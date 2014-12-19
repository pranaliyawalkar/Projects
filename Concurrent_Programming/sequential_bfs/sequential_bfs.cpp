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
int num_partitions;
pthread_mutex_t print;

struct node {
	int info;
	node *next;
};
 
class Queue {
	public:
		node *front;
		node *rear;
	public:
		Queue();
		~Queue();
		bool isEmpty();
		void enqueue(int);
		int dequeue();
		void display();
};

Queue queue[10];

void Queue::display(){
	node *p = this->front;
	if(front == NULL){
		cout<<"\nNothing to Display\n";
	}
	else{
		while(p!=NULL){
			cout << p->info << " ";
			p = p->next;
		}
	}
}
 
Queue::Queue() {
	front = NULL;
	rear = NULL;
}
 
Queue::~Queue() {
	delete front;
}
 
void Queue::enqueue(int data) {
	node *temp = new node();
	temp->info = data;
	temp->next = NULL;
	if(front == NULL){
		front = temp;
	}
	else{
		rear->next = temp;
	}
	rear = temp;
}
 
int Queue::dequeue() {
	node *temp;
	int value;
	if(front == NULL){
		cout<<"\nQueue is Emtpty\n";
	}
	else{
		temp = front;
		value = temp->info;
		front = front->next;
	}
	return value;
}
 
bool Queue::isEmpty() {
	return (front == NULL);
}

struct threadInfo{
	Queue q1;
	Queue q2;
	Queue q3;
	int level1, level2, level3;
};

struct combineInfo1{
	Queue q;
	int level;
};

struct combineInfo2{
	Queue q1;
	Queue q2;
};

struct threadInfo *thrArg;
 
class Graph {
	public:
		int n; 			// n is the number of vertices in the graph
		int **matrix; 	// matrix stores the edges between two vertices
		int *dis;		// Distance of each node from source node
		int height;		// Depth of BFS
	public:
		~Graph();
		void graphInit(int size);
		bool isConnected(int, int);
		void addEdge(int u, int v);
		void BFS(int );
};

Graph g;

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
	Queue Q;
 
	bool *explored = new bool[n+1];
	
	for (int i = 1; i <= n; ++i)
		explored[i] = false;
 
	Q.enqueue(s);
	explored[s] = true; 
	//cout << "Breadth first Search starting from vertex ";
	//cout << s << " : " << endl;
	height = 0;
	dis[s] = 0;
	int check = 0, prev = 0;
	queue[check].enqueue(s);
	while (!Q.isEmpty()) {
		int v = Q.dequeue();
		//cout << v << " ";
		
		for (int k = 0; k < n; ++k)
			if (isConnected(v, k) && !explored[k]) {
				Q.enqueue(k);
				dis[k] = dis[v]+1;
				check = dis[k];
				height = dis[k];
				queue[check].enqueue(k);
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
	node *p1 = arg->q.front;
	while(p1 != NULL){
		node *p2 = arg->q.front;
		while(p2 != NULL){
			val1 = p1->info; val2 = p2->info;
			if(val2 > val1 && g.matrix[val1][val2] == 1){
				node *p3 = arg->q.front;
				while(p3 != NULL){
					val3 = p3->info;
					if(val3 > val2 && g.matrix[val1][val3] == 1 && g.matrix[val2][val3] == 1){
						int z = c(level,2) + level*(num_partitions - level - 1) + c(num_partitions- level -1,2);
						if(z != 0){
							ret[val1] += (double)1/z;
							ret[val2] += (double)1/z;
							ret[val3] += (double)1/z;
						}
						else{
							ret[val1] += (double)1;
							ret[val2] += (double)1;
							ret[val3] += (double)1;
						}
					}
					p3 = p3->next;
				}
			}
			p2 = p2->next;
		}
		p1 = p1->next;
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
	node *p1 = arg->q1.front;
	while(p1 != NULL){
		node *p2 = arg->q2.front;
		while(p2 != NULL){
			val1 = p1->info; val2 = p2->info;
			if(g.matrix[val1][val2] == 1){
				node *p3 = arg->q2.front;
				while(p3 != NULL){
					val3 = p3->info;
					if(val3 > val2 && g.matrix[val1][val3] == 1 && g.matrix[val2][val3] == 1){
						int z = num_partitions - 2;
						ret[val1] += (double)1/z;
						ret[val2] += (double)1/z;
						ret[val3] += (double)1/z;
					}
					p3 = p3->next;
				}
			}
			p2 = p2->next;
		}
		p1 = p1->next;
	}
	
	p1 = arg->q1.front;
	while(p1 != NULL){
		node *p2 = arg->q1.front;
		while(p2 != NULL){
			val1 = p1->info; val2 = p2->info;
			if(val2 > val1 && g.matrix[val1][val2] == 1){
				node *p3 = arg->q2.front;
				while(p3 != NULL){
					val3 = p3->info;
					if(g.matrix[val1][val3] == 1 && g.matrix[val2][val3] == 1){
						int z = num_partitions - 2;
						ret[val1] += (double)1/z;
						ret[val2] += (double)1/z;
						ret[val3] += (double)1/z;
					}
					p3 = p3->next;
				}
			}
			p2 = p2->next;
		}
		p1 = p1->next;
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
	node *p = myInfo->q1.front;
	while(p!=NULL){
		arg[0].q.enqueue(p->info);
		p = p->next;
	}
	p = myInfo->q2.front;
	while(p!=NULL){
		arg[1].q.enqueue(p->info);
		p = p->next;
	}
	p = myInfo->q3.front;
	while(p!=NULL){
		arg[2].q.enqueue(p->info);
		p = p->next;
	}
	
	arg[0].level = lev1; arg[1].level = lev2; arg[2].level = lev3;
	rc = pthread_create(&thread[0], &attr, nodeIterator1, (void*)&arg[0]); 
	rc = pthread_create(&thread[1], &attr, nodeIterator1, (void*)&arg[1]); 
	rc = pthread_create(&thread[2], &attr, nodeIterator1, (void*)&arg[2]); 
	
	struct combineInfo2 arg2[2];
	if(lev1+1 == lev2){
		p = myInfo->q1.front;
		while(p!=NULL){
			arg2[0].q1.enqueue(p->info);
			p = p->next;
		}

		p = myInfo->q2.front;
		while(p!=NULL){
			arg2[0].q2.enqueue(p->info);
			p = p->next;
		}
		rc = pthread_create(&thread[3], &attr, nodeIterator2, (void*)&arg2[0]); 	
	}
	if(lev2+1 == lev3){
		p = myInfo->q2.front;
		while(p!=NULL){
			arg2[1].q1.enqueue(p->info);
			p = p->next;
		}
		p = myInfo->q3.front;
		while(p!=NULL){
			arg2[1].q2.enqueue(p->info);
			p = p->next;
		}
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
	g.BFS(0);
//	cout << g.height << endl;
	
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
				node *p = queue[i].front;
				while(p!=NULL){
					thrArg[nThr].q1.enqueue(p->info);
					p = p->next;
				}
			
				p = queue[j].front;
				while(p!=NULL){
					thrArg[nThr].q2.enqueue(p->info);
					p = p->next;
				}
			
				p = queue[k].front;
				while(p!=NULL){
					thrArg[nThr].q3.enqueue(p->info);
					p = p->next;
				}
			
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
		temp = (double*)status;
		for(j =0; j < size; j++){
			nTriangle[j] += temp[j];
		}
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
			if(g.matrix[i][j] == 1)
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
	return 0;
}
