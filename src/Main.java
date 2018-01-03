public class Main {

    public static void main(String[] args) {
        String S="E";//开始符
        String P[]={"E->aA|bB","A->cA|d","B->cB|d"};//规则集
        Grammar G=new Grammar(P,S);

        G.out();
        System.out.print(G.contains("bccd"));
//        Production a=new Production("A->c|B|d");
//        Production b=new Production("A->B|c|d");
//        if(a.equals(b)){
//            System.out.println("a==b");
//        }else {
//            System.out.println("a!=b");
//        }
//        Set<Production> aP=new HashSet<>();
//        aP.add(b);
//        aP.add(a);
//        Set<Production> aP2=new HashSet<>();
//        aP2.add(b);
//        aP2.add(a);
////        System.out.println(aP);
//        IE as=new IE(aP,"s",aP);
//        IE ab=new IE(aP2,"s",aP2);
//        System.out.println(as.hashCode()==ab.hashCode());

    }
}
