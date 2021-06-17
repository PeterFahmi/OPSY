import java.io.IOException;
import java.util.Arrays;

public class CPU {
    static String[] memory=new String[1000000];

    public static String nextProcess(){
        int pc = Integer.parseInt(memory[2]);
        // if pc points to null OR if it exceeds the last pcb [position 1000],
        // go back to the 1st pcb and return it.
        if(pc >= 500 || memory[pc] == null){
            pc = 3;
            memory[2] = pc+"";
        }
        String nextProc = memory[pc++];
        memory[2] = pc+"";
        return nextProc;
    }

    public static void  read_Allocate_programs(){
        int c=0;
        while(true){

            try {
                String path="Program "+(++c)+".txt";
                String [] instructions=Main.readProgram(path);
                allocate_in_memory(instructions,c);
            } catch (IOException e) {
                break;
            }
        }
    }

    private static void allocate_in_memory(String[] instructions,int num) {
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

    public static void main(String[] args) {
        memory[0]="500";
        memory[1]="2";
        memory[2]="3";
        read_Allocate_programs();
        printMemory();
    }

    private static void printMemory() {
        for (int i = 0; i < memory.length; i++) {
            if(memory[i]!=null){
                System.out.println(i+" "+memory[i]);
            }
        }
    }

    public static void executeInstruction(String process){
        int pc=Integer.parseInt(memory[Integer.parseInt(process)+2]);
        String current_inst=memory[pc];
        String remaining_inst=Main.interpret2(current_inst);
        if(remaining_inst==null){
            memory[Integer.parseInt(process)+2]+=1;
            //if end of code end process
        }else{
            memory[pc]=remaining_inst;
        }
    }
}
