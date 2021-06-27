import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

public class CPU {
    static String[] memory=new String[1000000];



    public static void  read_Allocate_programs(){
        int c=0;
        while(true){

            try {
                String path="Program "+(++c)+".txt";
                String [] instructions=readProgram(path);
                allocate_in_memory(instructions);
            } catch (IOException e) {
                break;
            }
        }
    }

    private static void allocate_in_memory(String[] instructions) {
        int lastSpace=Integer.parseInt(memory[0]);
        int lastProcessAddress=Integer.parseInt(memory[1]);

        int id=1;
        if(lastProcessAddress!=2){
            id=Integer.parseInt(memory[Integer.parseInt(memory[lastProcessAddress])])+1;
        }
        String state="ready";
        int pc=Integer.parseInt(memory[0])+1004;
        int boundaries=instructions.length+1004;//XXX-XXX
        String Bounds=lastSpace+"-"+(lastSpace+boundaries-1);
        if(lastProcessAddress==499){
            System.out.println("Error OPSY HEAP out of bound exception");
        }else{
            memory[++lastProcessAddress]=lastSpace+"";
            memory[1]=lastProcessAddress+"";
        }
        memory[lastSpace++]=id+"";
        memory[lastSpace++]=state+"";
        memory[lastSpace++]=pc+"";
        memory[lastSpace++]=Bounds;
        lastSpace+=1000;
        for (int i = 0; i < instructions.length; i++) {
            memory[lastSpace++]=instructions[i];
        }
        memory[0]=lastSpace+"";
    }
    public static String nextProcess(){
        int pc = Integer.parseInt(memory[2]);
        // if pc points to null OR if it exceeds the last pcb [position 400],
        // go back to the 1st pcb and return it.
        if(pc >= 500 || memory[pc] == null){
            pc = 3;
            memory[2] = pc+"";
        }
        String nextProc = memory[pc++];
        memory[2] = pc+"";
        return nextProc;
    }
    public static void run_All_Procs(){
        String nextProc=nextProcess();

        while(nextProc!=null){
            memory[Integer.parseInt(nextProc)+1]="running";
            System.out.println("--Process ID: "+memory[Integer.parseInt(nextProc)]);
            boolean go_on=executeInstruction(nextProc);
            if(go_on){
                if(!executeInstruction(nextProc)) {
                    deleteShift(nextProc);
                }else{
                    memory[Integer.parseInt(nextProc)+1]="ready";
                }
            }else{
                deleteShift(nextProc);
            }
            nextProc=nextProcess();
        }
    }

    public static boolean executeInstruction(String process){
        int pc=Integer.parseInt(memory[Integer.parseInt(process)+2]);
        String boundaries=memory[Integer.parseInt(process)+3];
        String[] temp=boundaries.split("-");
        int lastInst=Integer.parseInt(temp[1]);

        String current_inst=memory[pc];
        //System.out.println(current_inst);
        interpret(current_inst,process);
        pc++;
        memory[Integer.parseInt(process)+2]=pc+"";
        if(pc>lastInst)
            return false;

        return true;

    }
    private static void printMemory() {
        for (int i = 0; i < memory.length; i++) {
            if(memory[i]!=null){
                System.out.println(i+" "+memory[i]);
            }
        }
    }

    /**
     * Given process to be deleted, this method deletes this PCB and shifts all
     * the next PCBs back.
     * @param delProc
     */
    public static void deleteShift(String delProc){
        memory[Integer.parseInt(delProc)+1]="terminated";
        int idx=0;
        for(int i = 3; i <= 499; i++)
            if(memory[i].equals(delProc)) {
                idx = i;
                break;
            }
        // delete PCB elements
        memory[idx] = null;

        // shift deleted PCB to the end of the queue.
        int nextPosition = idx+1;
        while(memory[nextPosition] != null) {
            swap(idx, nextPosition);
            idx++;
            nextPosition++;
        }
        memory[2] = (Integer.parseInt(memory[2])-1)+"";
        memory[1] = (Integer.parseInt(memory[1])-1)+"";


        int instructions=calculateNumberOfInstructions(delProc);
        int slices=(int)Math.ceil(instructions/2.0);
        //System.out.println("slices"+slices);
        String res="";
        for (int i = 0; i < slices-1; i++) {
            res+="2,";
        }
        if(instructions%2==0){
            res+="2";
        }else{
            res+="1";
        }
       // System.out.println("res"+res);
        System.out.println("Process "+memory[Integer.parseInt(delProc)]+" has terminated with quanta slices: "+res);
    }

    private static int calculateNumberOfInstructions(String delProc) {
        String boundaries=memory[Integer.parseInt(delProc)+3];
        String[] temp=boundaries.split("-");
        int lowerBound=Integer.parseInt(temp[0]);
        int lastInst=Integer.parseInt(temp[1]);
       // System.out.println(lastInst-lowerBound-1003);
        return lastInst-lowerBound-1003;
    }

    /**
     * given index of two PCBs, all their elements are swapped.
     */
    private static void swap(int a, int b){
        String temp = memory[a];
        memory[a] = memory[b];
        memory[b] = temp;
    }
    public static String readMemory(String varName,String process){

        String bounds=(memory[Integer.parseInt(process)+3]);
        String[] bound=bounds.split("-");
        int low=Integer.parseInt(bound[0])+4;
        for (int i = low; i < low+1000; i++) {
            if(memory[i]==null) {
                return null;
            }
            String []var=memory[i].split("#%#");
            if(var[0].equals(varName)){
                System.out.println("---Memory read from address "+i+" "+varName+":"+var[1]);
                return var[1];
            }
        }return null;
    }

    public static void writeMemory(String varName,String value,String process){
        String bounds=(memory[Integer.parseInt(process)+3]);
        String[] bound=bounds.split("-");
        int low=Integer.parseInt(bound[0])+4;
        for (int i = low; i < low+1000; i++) {
            if(memory[i]==null){
                System.out.println("---Memory write to address "+i+" "+varName+":"+value);
                memory[i]=varName+"#%#"+value;
                break;
            }
            String []var=memory[i].split("#%#");
            if(var[0].equals(varName)){
                System.out.println("---Memory write to address "+i+" "+varName+":"+value);
                memory[i]=varName+"#%#"+value;
                break;
            }
        }
    }
    public static String[] readProgram(String path)throws IOException {
        File file = new File(path);

        BufferedReader br = new BufferedReader(new FileReader(file));

        ArrayList<String> als=new ArrayList<String>();

        String st;
        while ((st = br.readLine()) != null){
            als.add(st);
        }
        String[] res =new String[als.size()];
        for (int i = 0; i < als.size(); i++) {
            res[i]=als.get(i);
        }
        return res;
    }

    public static void interpret(String instruction,String process) {//print add readfile x readfile y
        Stack<String> stk=new Stack<String>();
        Stack<String> stk2=new Stack<String>();

        String[] split=instruction.split(" ");
        for (int i = 0; i < split.length; i++) {
            stk.push(split[i]);
        }
        while(stk.size()+stk2.size()>1){
            String cur=stk.pop();
            switch (cur){
                case "print":Main.print(stk2.pop(),process); break;
                case "add":stk.push(Main.add(stk2.pop(),stk2.pop(),process));break;
                case "assign":Main.assign(stk2.pop(),stk2.pop(),process); break;
                case "readFile":stk.push(Main.readFile(stk2.pop(),process)); break;
                case "writeFile":Main.writeFile(stk2.pop(),stk2.pop(),process);break;
                case "input":stk.push(Main.input());break;
                default:stk2.push(cur);
            }
        }

    }



    public static void main(String[] args) {
        memory[0]="500";
        memory[1]="2";
        memory[2]="3";
        read_Allocate_programs();
        printMemory();
        System.out.println("----------------------run----------------------");
        run_All_Procs();
    }
}
