import java.util.*;

class Hello{
    //This function will display a menu to choose the option to execute
    public static void choice(){
        System.out.println("**********************************************************");
        System.out.println("*         <------Enter your choice------>                *");
        System.out.println("*                                                        *");
        System.out.println("*     type i and then integer to insert new value        *");
        System.out.println("*     type s and then integer to search new value        *");
        System.out.println("*       type d to display directory and buckets          *");
        System.out.println("*           type e to exit the application               *");
        System.out.println("*                                                        *");
        System.out.println("**********************************************************");
    }

    public static void main(String args[]){
        //Assuming initial values of blocking factor and global depth
        int bfr=3,globalDepth=1;

        DIRECTORY dir=new DIRECTORY(globalDepth,bfr);
        System.out.println("Directory has been created with global depth=1 and bfr=3");

        //Display the options
        choice();

        Scanner sc = new Scanner(System.in);
        char s;
        do
        {
            s = sc.next().charAt(0);;
            if(s=='i'){
                int value=0;
                try{
                    value=sc.nextInt();
                    dir.insert(value,false);
                }
                catch(InputMismatchException e){
                    System.out.println("Please enter a valid integer!!!");
                }
                //Second argument is reinserted which will help us to see whether we are moving while splitting buckets or inserting directly

            }
            else if(s=='s'){
                int value=0;
                try{
                    value=sc.nextInt();
                    dir.search(value);
                }
                catch(InputMismatchException e){
                    System.out.println("Please enter a valid integer!!!");
                }

            }
            else if(s=='d'){
                dir.display(false);
            }
        } while(s!='e');
    }
}

class DIRECTORY{
    int globalDepth,bfr;
    ArrayList <BUCKET> buckets=new ArrayList<BUCKET> (0);  //buckets will be storing individual buckets of the directory
    DIRECTORY(int globalDepth,int bfr){
        this.globalDepth=globalDepth;
        this.bfr=bfr;

        //initially we are creating only 2 buckets corresponding to 0 and 1
        for(int i=0;i<(1<<globalDepth);++i){
            buckets.add(new BUCKET(globalDepth,bfr));
        }
    }

    int pairIndex(int bucketNumber,int depth){
        return (bucketNumber^(1<<(depth-1)));
    }

    void expandDirectory(){
        //this will double the buckets
        for(int i=0;i<(1<<globalDepth);++i){
            buckets.add(buckets.get(i));
        }
        ++globalDepth;
    }

    void splitOverflown(int bucketNumber){
        int localDepth,pairIndex,index_diff,dir_size,i;
        HashSet<Integer> temp = new HashSet<Integer>();

        localDepth=buckets.get(bucketNumber).increaseDepth();

        //Directory needs to be expanded if the local depth is exceeding global depth
        if(localDepth>globalDepth){
            expandDirectory();
        }
        pairIndex=pairIndex(bucketNumber,localDepth);
        buckets.set(pairIndex,new BUCKET(localDepth,bfr));
        temp=buckets.get(bucketNumber).copy();
        buckets.get(bucketNumber).clear();
        index_diff = 1<<localDepth;
        dir_size = 1<<globalDepth;
        for(i=pairIndex-index_diff;i>=0;i-=index_diff){
            buckets.set(i,buckets.get(pairIndex));
        }
        for(i=pairIndex+index_diff;i<dir_size;i+=index_diff){
            buckets.set(i,buckets.get(pairIndex));
        }
        Iterator<Integer> it = temp.iterator();
        while(it.hasNext()){
            insert(it.next(),true);
        }
    }

    //In search function firstly the bucket where key needs to be inserted is found and then we check that bucket
    void search(int key){
        int bucketNumber=hashFunction(key);
        System.out.println("Searching key "+key+" in bucket "+bucketID(bucketNumber));
        buckets.get(bucketNumber).search(key);
    }
    int hashFunction(int n){
        return (n&((1<<globalDepth)-1));
    }

    //bucketID is a function which will be converting number to binary string
    String bucketID(int num){
        int d;
        String str;
        d=buckets.get(num).getDepth();
        str="";
        while(num>0 && d>0)
        {
            str = (num%2==0?"0":"1")+str;
            num/=2;
            d--;
        }
        while(d>0)
        {
            str = "0"+str;
            d--;
        }
        return str;
    }

    void insert(int key,boolean reinserted){
        int bucketNumber=hashFunction(key);
        int status=buckets.get(bucketNumber).insert(key);
        if(status==1){
            if(!reinserted){
                System.out.println("Inserted "+key+" in bucket "+bucketID(bucketNumber));
            }
            else{
                System.out.println("Moved "+key+" to bucket "+bucketID(bucketNumber));
            }
        }
        else if(status==0){
            splitOverflown(bucketNumber);
            insert(key, reinserted);
        }
        else{
            System.out.println("Key "+key+" already exists in bucket "+bucketID(bucketNumber));
        }
    }

    //duplicates will be used to incase the bucket is not split and we then want to print it as different or not
    void display(boolean duplicates){
        String str;
        //this set is used to mark if a particular value has been printed before
        //this will have no significance if we are printing duplicates
        HashSet<String> displayed = new HashSet<String>();
        System.out.println("Global depth is : "+globalDepth);
        for(int i=0;i<buckets.size();++i){
            int extra=buckets.get(i).getDepth();
            str=bucketID(i);
            if(duplicates || !displayed.contains(str)){
                displayed.add(str);
                //will leave space while printing buckets whose local depth is less than the global depth
                for(int j=extra;j<=globalDepth;++j){
                    System.out.print(" ");
                }
                System.out.print(str+" => ");
                buckets.get(i).display();
            }
            System.out.println();
        }
    }

}

class BUCKET{
    int depth,size;
    HashSet<Integer> values = new HashSet<Integer>();
    BUCKET(int depth,int size){
        this.depth=depth;
        this.size=size;
    }
    //this function will be used to get a status whether the key is already there / bucket has to be split /add key
    int insert(int key){
        if(values.contains(key)){
            return -1;
        }
        if(isFull()){
            return 0;
        }
        values.add(key);
        return 1;
    }
    HashSet<Integer> copy(){
        HashSet<Integer> temp = new HashSet<Integer>();
        Iterator<Integer> it =values.iterator();
        while(it.hasNext()) {
            temp.add(it.next());
        }
        return temp;
    }
    //Display the particular bucket
    void display(){
        Iterator<Integer> it =values.iterator();
        while(it.hasNext()){
            System.out.print(it.next()+" ");
        }
    }
    //To check if a particular bucket is full or not
    boolean isFull(){
        if(values.size()==size){
            return true;
        }
        else{
            return false;
        }
    }
    //get depth of that particular bucket
    int getDepth()
    {
        return depth;
    }
    //increase depth while splitting
    int increaseDepth() {
        ++depth;
        return depth;
    }
    //clear that bucket
    void clear(){
        values.clear();
    }
    //searching in that bucket
    void search(int key){
        Iterator<Integer> it =values.iterator();
        if(values.contains(key)){
            System.out.println("Key exists in a bucket");
        }
        else{
            System.out.println("This key does not exists");
        }
    }
}
