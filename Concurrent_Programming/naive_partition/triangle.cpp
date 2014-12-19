#include <iostream>
#include <iterator>
#include <functional>
#include <stdlib.h>
#include <stdio.h>
#include <math.h>
//#include <cilk/cilk.h>
#include <sys/time.h>
#include <pthread.h>
#include <sched.h>

using namespace std;
int size;	//No.of vertices of graph
int n, m; // vertices, edges
int** matrix;
int num_partitions;
int partition_size = 1200;
pthread_mutex_t print;

int c(int a, int b)
{
	if(b == 2)
		return (a*(a-1))/2;
	else
		return (a*(a-1)*(a-2))/6;
}

int h(int a)
{
	return a/partition_size;
}

void *nodeIterator1(void *a)
{
	int i, j, k;
	int *arg = (int*)a;
	double *ret;
	ret = new double[size]();
	for(i = 0; i < size; i++)
		ret[i] = 0;
	
	for(i = arg[0]; i < arg[0]+arg[1]; i++){
		for(j = arg[0]; j < arg[0]+arg[1]; j++){
			if(j>i && matrix[i][j] == 1){
				for(k = arg[0]; k < arg[0]+arg[1]; k++){
					if(k>j && matrix[i][k] == 1 && matrix[j][k] == 1){
						int z = c(h(i),2) + h(i)*(num_partitions - h(i) - 1) + c(num_partitions-h(i)-1,2);
						ret[i] += (double)1/z;
						ret[j] += (double)1/z;
						ret[k] += (double)1/z;
					}
				}
			}
		}
	}
	pthread_mutex_lock(&print);
	//cout<<"check in nodeIterator1 arguments are "<<arg[0]<< " for first node "<<ret[0]<<endl;
	pthread_mutex_unlock(&print);
	pthread_exit((void*)ret);
}

void *nodeIterator2(void *a)
{
	int i, j, k;
	int *arg = (int*)a;
	double *ret;
	ret = new double[size]();
	for(i = 0; i < size; i++)
		ret[i] = 0;
	
	for(i = arg[0]; i < arg[0]+arg[1]; i++){
		for(j = arg[2]; j < arg[2]+arg[3]; j++){
			if(matrix[i][j] == 1){
				for(k = arg[2]; k < arg[2]+arg[3]; k++){
					if(k>j && matrix[i][k] == 1 && matrix[j][k] == 1){
						int z = num_partitions - 2;
						ret[i] += (double)1/z;
						ret[j] += (double)1/z;
						ret[k] += (double)1/z;
						if(i==0)
						{
							pthread_mutex_lock(&print);
							//cout<<"see here first node "<<ret[0]<<endl;
							pthread_mutex_unlock(&print);
						}
					}
				}
			}
		}
	}
	pthread_mutex_lock(&print);
	//cout<<"check once first node "<<ret[0]<<endl;
	pthread_mutex_unlock(&print);
	for(i = arg[0]; i < arg[0]+arg[1]; i++){
		for(j = arg[0]; j < arg[0]+arg[1]; j++){
			if(j > i && matrix[i][j] == 1){
				for(k = arg[2]; k < arg[2]+arg[3]; k++){
					if(matrix[i][k] == 1 && matrix[j][k] == 1){
						int z = num_partitions - 2;
						ret[i] += (double)1/z;
						ret[k] += (double)1/z;
						ret[j] += (double)1/z;
					}
				}
			}
		}
	}
	pthread_mutex_lock(&print);
	//cout<<"check in nodeIterator2 arguments are "<<arg[0]<<"  "<<arg[2]<< " for first node "<<ret[0]<<endl;
	pthread_mutex_unlock(&print);
	pthread_exit((void*)ret);
}

void *nodeIterator3(void *a)
{
	int  i, j, k;
	int *arg = (int*)a;
	double *ret;
	pthread_mutex_lock(&print);
	////cout<<"checking in nodeIterator3"<<endl;
//	//cout<<arg[0]<<" "<<arg[2]<<" "<<arg[4]<<endl;
	pthread_mutex_unlock(&print);
	ret = new double[size]();
	for(i = 0; i < size; i++)
		ret[i] = 0;
	
	for(i = arg[0]; i < arg[0]+arg[1]; i++){
		for(j = arg[2]; j < arg[2]+arg[3]; j++){
			if(matrix[i][j] == 1){
				for(k = arg[4]; k < arg[4]+arg[5]; k++){
					if(matrix[i][k] == 1 && matrix[j][k] == 1){
						pthread_mutex_lock(&print);	
						pthread_mutex_unlock(&print);	
						ret[i] += 1;
						ret[j] += 1;
						ret[k] += 1;
					}
				}
			}
		}
	}
	pthread_mutex_lock(&print);
	//cout<<"check in nodeIterator3 arguments are "<<arg[0]<<"  "<<arg[4] <<" for first node "<<ret[0]<<endl;
	pthread_mutex_unlock(&print);
	
	pthread_exit((void*)ret);
}


void *combine(void *arr)
{
	int *arg = (int*)arr;
	pthread_attr_t attr;
	
	pthread_attr_init(&attr);
	pthread_attr_setdetachstate(&attr, PTHREAD_CREATE_JOINABLE);
	pthread_mutex_lock(&print);
	//cout<<"checking in combine"<<endl;
//	//cout<<*((int *) arr)<<" "; /* first element */
//	//cout<<*((int *) arr + 1)<<" "; /* second element */
//	//cout<<*((int *) arr + 2)<<endl;
	//cout<<arg[0]<<" "<<arg[2]<<" "<<arg[4]<<endl;
	pthread_mutex_unlock(&print);
	
	int r1, n1, r2, n2, r3, n3;
	r1 = arg[0]; n1 = arg[1]; r2 = arg[2]; n2 = arg[3]; r3 = arg[4]; n3 = arg[5];
	int i, j, k, rc;
	pthread_t thread[7];
	void *status;
	
	int arg_part1[6], arg_part2[6], arg_part3[6];
	int arg_part4[6], arg_part5[6], arg_part6[6], arg_part7[6];
	for(i = 0; i < 6 ; i++){
		arg_part1[i] = -1;
		arg_part2[i] = -1;
		arg_part3[i] = -1;
		arg_part4[i] = -1;
		arg_part5[i] = -1; arg_part6[i] = -1; arg_part7[i] = -1;
	}
	
	arg_part1[0] = r1; arg_part1[1] = n1;
	arg_part2[0] = r2; arg_part2[1] = n2;
	arg_part3[0] = r3; arg_part3[1] = n3;
	arg_part4[0] = r1; arg_part4[1] = n1; arg_part4[2] = r2; arg_part4[3] = n2;
	arg_part5[0] = r2; arg_part5[1] = n2; arg_part5[2] = r3; arg_part5[3] = n3;
	arg_part6[0] = r1; arg_part6[1] = n1; arg_part6[2] = r3; arg_part6[3] = n3;
	arg_part7[0] = r1; arg_part7[1] = n1; arg_part7[2] = r2; arg_part7[3] = n2; arg_part7[4] = r3; arg_part7[5] = n3; 
	
	rc = pthread_create(&thread[0], &attr, nodeIterator1, (void*)arg_part1); 
	rc = pthread_create(&thread[1], &attr, nodeIterator1, (void*)arg_part2); 
	rc = pthread_create(&thread[2], &attr, nodeIterator1, (void*)arg_part3); 
	rc = pthread_create(&thread[3], &attr, nodeIterator2, (void*)arg_part4); 
	rc = pthread_create(&thread[4], &attr, nodeIterator2, (void*)arg_part5); 
	rc = pthread_create(&thread[5], &attr, nodeIterator2, (void*)arg_part6); 
	rc = pthread_create(&thread[6], &attr, nodeIterator3, (void*)arg_part7); 

	double *local_count;
	local_count = new double[size]();
	double *loc;
	loc = new double[size]();
	for(i = 0; i < 7 ; i++){
		rc = pthread_join(thread[i], &status);
		
		loc = (double*)status;
		pthread_mutex_lock(&print);
		//cout<<"check for first node "<<loc[0]<<endl;
		pthread_mutex_unlock(&print);
		for(j = 0; j < size; j++){
			local_count[j] += loc[j];
		}
		
	    if (rc) {
			printf("ERROR; return code from pthread_join() is %d\n", rc);
			exit(-1);
		}
	}
	
		////cout<<"local_count "<<endl;
		//for(int l=0;l<size;l++)
			////cout<<l <<"  "<<local_count[l]<<endl;
		pthread_mutex_unlock(&print);
	pthread_exit((void*)local_count);
}

int main(int argc, char* argv[])
{
	pthread_t *thread;
	pthread_attr_t attr;
	int rc, i, j, k;
	long t;
	void *status;

	pthread_attr_init(&attr);
	pthread_attr_setdetachstate(&attr, PTHREAD_CREATE_JOINABLE);
	
	cin >> n >> m;
	size = n;
	matrix = (int**)malloc(n*sizeof(int*));
	for(i = 0; i < n; i++)
		matrix[i] = (int*)malloc(n*sizeof(int));
		
	int l;
	num_partitions = n/partition_size;
	if(n%partition_size!=0)
		num_partitions++;
  
	for(l = 0; l < m; l++){
		int u, v;
		cin >> u >> v;
		matrix[(u)][v] = 1;
		matrix[(v)][u] = 1;
	}
  	int nThr = 0;
  	nThr = c(num_partitions,3);
  	thread = (pthread_t*) malloc((nThr)*sizeof(pthread_t));
  	nThr = 0;
  	pthread_mutex_init(&print,NULL);
	int n_i, n_j, n_k;
	
		struct timeval startTime, endTime;
	double elapsedTime;
	gettimeofday(&startTime, NULL); 
	double t1 = startTime.tv_sec + (startTime.tv_usec/1000000.0);  

	
	for(i = 0; i < num_partitions; i++){
		for(j = i + 1; j < num_partitions; j++){
			for(k = j + 1; k < num_partitions; k++){
				n_i = n_j = n_k = partition_size;
				if(k == num_partitions -1 && n % partition_size != 0){
					n_k = n % partition_size;
				}
				int *arg_new;
				arg_new = new int[6];
				arg_new[0] = i * partition_size; arg_new[1] = n_i;
				arg_new[2] = j * partition_size; arg_new[3] = n_j;
				arg_new[4] = k * partition_size; arg_new[5] = n_k;
				pthread_mutex_lock(&print);
				//cout<<"check before pthread create"<<endl;
				//cout<<arg_new[0]<<" "<<arg_new[2]<<" "<<arg_new[4]<<endl;
				pthread_mutex_unlock(&print);
				pthread_create(&thread[nThr], &attr, combine, (void*)arg_new);
				nThr++;
			}
		}
	}
	nThr = c(num_partitions,3);
	double nTriangle[size];
	double *temp;
	temp = new double[size];
	for(i = 0; i < size; i++)
		nTriangle[i] = 0;
	for(i = 0; i < nThr; i++){
		rc = pthread_join(thread[i], &status);
		////cout<<" check number of initial threads"<<endl;
		temp = (double*)status;
		for(j =0; j < size; j++){
			////cout<<j<<"  "<<temp[j]<<endl;
			nTriangle[j] += temp[j];
			
		}
	//	//cout<<endl;
	}
	double cnt = 0;
	double tri;
	for(i = 0; i < size; i++){
		tri = nTriangle[i]/3;
		cnt += tri;
	}
	cout << "Total number of triangles : "<<cnt << endl;
	
	//Finding clustering coefficient
	int neighbors = 0;
	double avg_clustering_coefficient = 0;
	for(i = 0; i < size; i++){
		neighbors = 0;
		for(j = 0; j < size; j++){
			if(matrix[i][j] == 1)
				neighbors++;
		}
		if(neighbors != 0)
			neighbors = c(neighbors, 2);	
		if(neighbors != 0){
			avg_clustering_coefficient += (double)nTriangle[i]/neighbors;
		}
	}
	
	cout << "Avg clustering coefficient : " << avg_clustering_coefficient/size  << endl;
	
	gettimeofday(&endTime, NULL);
	double t2 = endTime.tv_sec + (endTime.tv_usec/1000000.0);  
	elapsedTime = t2-t1;
	
	cout << "elapsedTime: " << elapsedTime << endl;
	
	
	pthread_exit(NULL);
}
