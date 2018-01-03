import java.util.*;

public class Grammar {
    private static final char Njump = 'ε';
    private static final char Dian = '･';
    Set<String> VN;//非终结符
    Set<String> VT;//终结符集
    Set<Production> P;//规则集
    String S;//开始符
    ArrayList<Set<Production>> IList;
    Set<IE> IESet;//活前缀项目集2活前缀项目集

    public Grammar(Set<String> VN, Set<String> VT, Set<Production> p, String s) {
        this.VN = VN;
        this.VT = VT;
        P = p;
        S = s;
    }

    public Grammar(String[] ip, String S) {
        this.S = "S'";
        VN = new HashSet<>();
        VT = new HashSet<>();
        P = new HashSet<>();
        VN.add(S);
        P.add(new Production(this.S + "->" + S));
        for (int i = 0; i < ip.length; i++) {
            Production p = new Production(ip[i]);
            VN.add(p.getLeft());
            if (p.isSimple()) {
                P.add(p);
                String pr = p.getRight();
                for (int j = 0; j < pr.length(); j++) {
                    VT.add(String.valueOf(pr.charAt(j)));
                }
            } else {
                for (Production sp : p.toSimple()) {
                    P.add(sp);
                    String spr = sp.getRight();
                    for (int j = 0; j < spr.length(); j++) {
                        VT.add(String.valueOf(spr.charAt(j)));
                    }
                }
            }
        }
        VT.removeAll(VN);
        VT.remove(String.valueOf(Njump));

        Set<Production> I0 = new HashSet<>();//活前缀项目集的开始集
        I0.add(new Production(this.S + "->" + Dian + S));
        calculationCLOSURE(I0);
        IList =new ArrayList<>();
        IList.add(I0);
//        System.out.println(I0);
        calculationDFA();
//        System.out.println(ISet);
//        System.out.println("s");

    }

    //对项目集I进行闭包运算
    private void calculationCLOSURE(Set<Production> I) {
        Set<String> nV = new HashSet<>();//点后面的非终结符
        int ISize;
        do {
            ISize = I.size();
            for (Production i : I) {
                String iRight = i.right;
                int Di = iRight.indexOf(Dian);
                if (Di+1 < iRight.length()) {
                    String inV = iRight.substring(Di+1, Di +2);
                    if (VN.contains(inV)) {
                        nV.add(inV);
                    }
                }
            }
            for (Production ip : P) {
                if (nV.contains(ip.left)) {
                    I.add(ip.insertDian());//加点
                }
            }
        } while (ISize != I.size());
    }

    //求活前缀DFA,得到边集、项目集
    private void calculationDFA(){
        IESet =new HashSet<>();
        Queue<Set<Production>> queue = new LinkedList<>();
        queue.add(IList.get(0));
        Set<Production> nI;
        Map<String,Set<Production>> nIMap;

        while (!queue.isEmpty()){
            Set<Production> iI=queue.poll();
            nIMap=new HashMap<>();
            //求核
            for (Production i : iI) {
                String iRight = i.right;
                int Di = iRight.indexOf(Dian);
                if (Di+1 < iRight.length()) {
                    String iV = iRight.substring(Di+1, Di +2);
                    nI=nIMap.get(iV);
                    if(nI==null){
                        nI=new HashSet<>();
                        nI.add(i.moveDian());//移点
                        nIMap.put(iV,nI);
                    }else{
                        nI.add(i.moveDian());
                    }
                }
            }
            //求闭包
            int iList=IList.indexOf(iI);
            for (String v:nIMap.keySet()) {
                nI=nIMap.get(v);
                calculationCLOSURE(nI);

                int jList=IList.indexOf(nI);
                if(jList==-1){
                    queue.add(nI);
                    IList.add(nI);
                    jList=IList.size()-1;
                }
                IESet.add(new IE(iList,v,jList));
            }
            nIMap.clear();
        }
    }
    public void out() {
        System.out.println("VN:");
        for (Object iVN : VN) {
            System.out.println(iVN);
        }
        System.out.println("VT:");
        for (Object iVT : VT) {
            System.out.println(iVT);
        }
        System.out.println("P:");
        for (Object ip : P) {
            System.out.println(ip.toString());
        }
        System.out.println("S:");
        System.out.println(S);
        System.out.println("IESet:");
        System.out.println(IESet);
    }
}
