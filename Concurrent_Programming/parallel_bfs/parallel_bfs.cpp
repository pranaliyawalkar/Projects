#include <iostream>
#include <iterator>
#include <functional>
#include <stdlib.h>
#include <stdio.h>
#include <math.h>
#include <cilk/cilk.h>
#include <sys/time.h>
#include <pthread.h>
#include <sched.h>
#include <list>
#include <cilk/reducer_list.h>
#include <vector>

#define GRAINSIZE 10

using namespace std;

int size;	//No.of vertices of graph
int n, m; // vertices, edges
int num_partitions;
pthread_mutex_t print;

vector< list<int> > myList;

struct node {
	int info;
	node *next;
};

struct threadInfo{
	list<int> *q1;
	list<int> *q2;
	list<int> *q3;
	int level1, level2, level3;
};

struct combineInfo1{
	list<int> *q;
	int level;
};

struct combineInfo2{
	list<int> *q1;
	list<int> *q2;
};

struct threadInfo *thrArg;
 
class Graph {
	public:
		int n; /// n is the number of vertices in the graph
		int **matrix; /// A stores the edges between two vertices
		int *dis;
		int height;
	public:
		~Graph();
		void graphInit(int size);
		bool isConnected(int, int);
		void addEdge(int u, int v);
		void BFS(int );
  void PBFS(int);
private:
  void PBFS_process_layer(list<int>* Vparent, 
			  list<int>::iterator start,
			  list<int>::iterator end,
			  cilk::reducer_list_append<int>& Vchild,
			  int plen,
			  int height);
};

Graph g;

void Graph::PBFS(int s){
  dis[s] = 0;
  height = 0;
  // contains lists of nodes in a partition
  //vector< list<int> > partitions_lists; // !! assuming it to be global 
  list<int> V0; // root
  list<int> *Vp; // parent layer pointer
  Vp = &V0;
  V0.push_front(s); 
  myList.push_back(V0); // !!
  list<int> Vc_list;
  while(!Vp->empty()){
    cilk::reducer_list_append<int> Vc_red;
    PBFS_process_layer(Vp, Vp->begin(), Vp->end(), 
		       Vc_red, Vp->size(), height); 
    Vc_list = Vc_red.get_value();
    myList.push_back(Vc_list); 
    Vp = &Vc_list;
    // make current child the next iteration's parent
    height++;
    
  }
} 

void Graph::PBFS_process_layer(list<int>* Vparent, 
				    list<int>::iterator start,
				    list<int>::iterator end,
				    cilk::reducer_list_append<int>& Vchild,
				    int plen,
				    int height){
  
  if(plen < GRAINSIZE){
    for(; start != end; start++){
      cilk_for(int i = 0; i < n; i++){
	if(isConnected(*start, i) && dis[i] == -1){ // if unvisited neighbour 
	  dis[i] = height + 1;
	  Vchild->push_back(i);
	}
      }
    }
    return; 
  }
  list<int>::iterator mid = start;
  for(int i = 0; i < plen/2; i++){
    mid++;
  }
  cilk_spawn PBFS_process_layer(Vparent, start, mid, Vchild, plen/2, height);
  cilk_spawn PBFS_process_layer(Vparent, mid, end, Vchild, plen - plen/2, height);
  cilk_sync;
}


void Graph::graphInit(int size){
	int i, j;
	n = size;
	matrix = new int*[n];
	for (i = 0; i < n; ++i)
		matrix[i] = new int[n];
	for (i = 0; i < n; ++i)
		for (j = 0; j < n; ++j)
			matrix[i][j] = 0;
	dis = new int[n];
	for(i = 0; i < n ; i++)
		dis[i] = -1;
}
 
Graph::~Graph() {
    for (int i = 0; i < n; ++i)
   		delete [] matrix[i];
    delete [] matrix;
}

bool Graph::isConnected(int u, int v) {
	return (matrix[u][v] == 1);
}
 
void Graph::addEdge(int u, int v) {
	matrix[u][v] = matrix[v][u] = 1;
}
 

void Graph::BFS(int s) {
	list<int> Q;
 
	bool *explored = new bool[n+1];
	
	for (int i = 1; i <= n; ++i)
		explored[i] = false;
 
	Q.push_back(s);
	explored[s] = true; 
	height = 0;
	dis[s] = 0;
	int check = 0, prev = 0;
	myList[check].push_back(s);
	while (!Q.empty()) {
		int v = Q.front();
		Q.pop_front();
		//cout << v << " ";
		
		for (int k = 0; k < n; ++k)
			if (isConnected(v, k) && !explored[k]) {
				Q.push_back(k);
				dis[k] = dis[v]+1;
				check = dis[k];
				height = dis[k];
				myList[check].push_back(k);
				explored[k] = true;
			}
	}
	cout << endl;
	delete [] explored;
}


int c(int a, int b)
{
	if(b == 2)
		return (a*(a-1))/2;
	else
		return (a*(a-1)*(a-2))/6;
}

int h(int a)
{
	return 0;//a/partition_size;
}

void *nodeIterator1(void *a)
{
	int i, j, k;
	struct combineInfo1 *arg;
	arg = (struct combineInfo1 *)a;
	int level = arg->level;
	
	double *ret;
	ret = new double[size]();
	for(i = 0; i < size; i++)
		ret[i] = 0;
	
	int val1, val2, val3;
	
	for (list<int>::iterator it1= arg->q->begin(); it1 != arg->q->end(); ++it1){
		for (list<int>::iterator it2= arg->q->begin(); it2 != arg->q->end(); ++it2){
			val1 = *it1 ; val2 = *it2;
			if(val2 > val1 && g.matrix[val1][val2] == 1){
				for (list<int>::iterator it3= arg->q->begin(); it3 != arg->q->end(); ++it3){
					val3 = *it3;
					if(val3 > val2 && g.matrix[val1][val3] == 1 && g.matrix[val2][val3] == 1){
						int z = c(level,2) + level*(num_partitions - level - 1) + c(num_partitions- level -1,2);
						ret[val1] += (double)1/z;
						ret[val2] += (double)1/z;
						ret[val3] += (double)1/z;
					}
				}
			}
		}
	}
	
	pthread_exit((void*)ret);
}

void *nodeIterator2(void *a)
{
	int i, j, k;
	struct combineInfo2	*arg;
	arg = (struct combineInfo2 *)a;
	
	double *ret;
	ret = new double[size]();
	for(i = 0; i < size; i++)
		ret[i] = 0;
	
	int val1, val2, val3;
	
	for (list<int>::iterator it1= arg->q1->begin(); it1 != arg->q1->end(); ++it1){
		for (list<int>::iterator it2= arg->q2->begin(); it2 != arg->q2->end(); ++it2){
			val1 = *it1; val2 = *it2;
			if(g.matrix[val1][val2] == 1){
				for (list<int>::iterator it3= arg->q2->begin(); it3 != arg->q2->end(); ++it3){
					val3 = *it3;
					if(val3 > val2 && g.matrix[val1][val3] == 1 && g.matrix[val2][val3] == 1){
						int z = num_partitions - 2;
						ret[val1] += (double)1/z;
						ret[val2] += (double)1/z;
						ret[val3] += (double)1/z;
					}
				}
			}
		}
	}
	
	for (list<int>::iterator it1= arg->q1->begin(); it1 != arg->q1->end(); ++it1){
		for (list<int>::iterator it2= arg->q1->begin(); it2 != arg->q1->end(); ++it2){
			val1 = *it1; val2 = *it2;
			if(val2 > val1 && g.matrix[val1][val2] == 1){
				for (list<int>::iterator it3= arg->q2->begin(); it3 != arg->q2->end(); ++it3){
					val3 = *it3;
					if(g.matrix[val1][val3] == 1 && g.matrix[val2][val3] == 1){
						int z = num_partitions - 2;
						ret[val1] += (double)1/z;
						ret[val2] += (double)1/z;
						ret[val3] += (double)1/z;
					}
				}
			}
		}
	}
	pthread_exit((void*)ret);
}


void *combine(void *Arg)
{
	struct threadInfo *myInfo;
	myInfo = (struct threadInfo*)Arg;
	int lev1, lev2, lev3;
	
	pthread_t thread[5];
	void *status;
	int rc;
	pthread_attr_t attr;
	pthread_attr_init(&attr);
	pthread_attr_setdetachstate(&attr, PTHREAD_CREATE_JOINABLE);
	
	lev1 = myInfo->level1;
	lev2 = myInfo->level2;
	lev3 = myInfo->level3;
	
	
	struct combineInfo1 arg[3];
	arg[0].q = myInfo->q1;
	arg[1].q = myInfo->q2;
	arg[2].q = myInfo->q3;
	arg[0].level = lev1; arg[1].level = lev2; arg[2].level = lev3;
	
	rc = pthread_create(&thread[0], &attr, nodeIterator1, (void*)&arg[0]); 
	rc = pthread_create(&thread[1], &attr, nodeIterator1, (void*)&arg[1]); 
	rc = pthread_create(&thread[2], &attr, nodeIterator1, (void*)&arg[2]); 
	
	struct combineInfo2 arg2[2];
	if(lev1+1 == lev2){
		arg2[0].q1 = myInfo->q1;
		arg2[0].q2 = myInfo->q2;
		rc = pthread_create(&thread[3], &attr, nodeIterator2, (void*)&arg2[0]); 	
	}
	if(lev2+1 == lev3){
		arg2[1].q1 = myInfo->q2;
		arg2[1].q2 = myInfo->q3;
		rc = pthread_create(&thread[4], &attr, nodeIterator2, (void*)&arg2[1]); 		
	}
	

	int i, j, k;
	double *local_count;
	local_count = new double[size]();
	double *loc;
	loc = new double[size]();
	for(i = 0; i < 5 ; i++){
		if(i == 0 || i == 1 || i == 2|| (i == 4 && lev2 + 1 == lev3) || (i == 3 && lev1+1 == lev2) ){
			rc = pthread_join(thread[i], &status);
			loc = (double*)status;
			for(j = 0; j < size; j++){
				local_count[j] += loc[j];
			}
		
			if (rc) {
				printf("ERROR; return code from pthread_join() is %d\n", rc);
				exit(-1);
			}
		}
	}
	
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
	
	int l;
	g.graphInit(n);  
	for(l = 0; l < m; l++){
		int u, v;
		cin >> u >> v;
		g.addEdge(u,v);
	}
	
	int n_i, n_j, n_k;

	struct timeval startTime, endTime;
	double elapsedTime;
	gettimeofday(&startTime, NULL); 
	double t1 = startTime.tv_sec + (startTime.tv_usec/1000000.0);  

	//Parallel BFS
	g.PBFS(0);
	//cout << myList.size() << "\n";
	//cout << g.height << endl;
	/*
	for(int a = 0; a < myList.size(); a++){
	  cout << "["<< a << "]\n";
	  list<int> l = myList[a];
	  list<int>::iterator it = l.begin();
	  cout << "<"<< l.size() << ">\n";
	  for(; it != l.end(); it++){
	    cout << *it << " ";
	  }
	  cout << "\n---\n";
	}
	*/
	
	num_partitions = g.height + 1;
	
	int nThr = 0;
  	nThr = c(num_partitions,3);
  	
  	//cout << "nThr : " << nThr <<  endl;
  	thrArg = new threadInfo[nThr];
  	thread = (pthread_t*) malloc((nThr)*sizeof(pthread_t));
  	
  	nThr = 0;
  	pthread_mutex_init(&print,NULL);
	
	
	/*for(i = 0; i < g.dis[n-1] ; i++){
		queue[i].display();
		cout << endl;
	}*/
	
	
	for(i = 0; i < num_partitions; i++){
		for(j = i + 1; j < num_partitions; j++){
			for(k = j + 1; k < num_partitions; k++){
				thrArg[nThr].q1	= &myList[i];
				thrArg[nThr].q2	= &myList[j];
				thrArg[nThr].q3	= &myList[k];				
				thrArg[nThr].level1 = i; thrArg[nThr].level2 = j;
				thrArg[nThr].level3 = k;
				pthread_create(&thread[nThr], &attr, combine, (void*)&thrArg[nThr]);
				nThr++;
			}
		}
	}
	

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
	}
	double cnt = 0;
	for(i = 0; i < size; i++){
		nTriangle[i] /= 3;
		cnt += nTriangle[i];
		//cout << i << " " << nTriangle[i] << endl;
	}
	cout << "total "<<cnt << endl;
	gettimeofday(&endTime, NULL);
	double t2 = endTime.tv_sec + (endTime.tv_usec/1000000.0);  
	elapsedTime = t2-t1;
	
	cout << "elapsedTime: " << elapsedTime << endl;
	
	//pthread_exit(NULL);
       
	return 0;
}
