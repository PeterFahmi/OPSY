import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

public class Main {

//    public static String interpret2(String instruction) {//print add readfile x readfile y
//        Stack<String> stk=new Stack<String>(); //1) print c       2) add c input
//        Stack<String> stk2=new Stack<String>();
//
//        String[] split=instruction.split(" ");
//        for (int i = 0; i < split.length; i++) {
//            stk.push(split[i]);
//        }
//        boolean done=false;
//        while(!done){
//            String cur=stk.pop();
//            switch (cur){
//                case "print":print(stk2.pop(), process);done=true; break;
//                case "add":stk.push(add(stk2.pop(),stk2.pop(), process));done=true;break;
//                case "assign":assign(stk2.pop(),stk2.pop(), process);done=true; break;
//                case "readFile":stk.push(readFile(stk2.pop(), process));done=true; break;
//                case "writeFile":writeFile(stk2.pop(),stk2.pop(), process);done=true;break;
//                case "input":stk.push(input());done=true;break;
//                default:stk2.push(cur);
//            }
//        }
//        if(stk.size()+stk2.size()>1){
//            String s="";
//            while(!stk.isEmpty()){
//                s=stk.pop()+" "+s;
//            }
//            while(!stk2.isEmpty()){
//                s=s+stk2.pop()+" ";
//            }
//            s=s.substring(0,s.length()-1);
//            return s;
//        }else{
//            return null;
//        }
//
//    }

    public static String input() {
        Scanner sc = new Scanner(System.in);
        return "_ "+sc.nextLine();
    }


    /**
     * This method assigns the input in the prompt to variable varName
     * @param varName
     * @param process
     */
    public static void assign(String varName, String value, String process){
        String rm=CPU.readMemory(value,process);
        if(rm!=null){//checks if value is variable
            CPU.writeMemory(varName, rm,process);
        }else{
            if(value.length()>2&& value.charAt(0)=='_'&&value.charAt(1)==' '){
                value=value.substring(2);
            }
            CPU.writeMemory(varName, value,process);
        }
    }
    public static String add(String a, String b, String process){
        int first;
        int second;
        try {
            String s = CPU.readMemory(a, process);
            first = Integer.parseInt(s);


            String s1 = CPU.readMemory(b, process);
            if (s1 != null) {
                second = Integer.parseInt(s1);
            } else {
                if (b.length() > 2 && b.charAt(0) == '_' && b.charAt(1) == ' ') {
                    b = b.substring(2);
                }
                second = Integer.parseInt(b);
            }
            int x = first + second;
            CPU.writeMemory(a, x + "",process);
            return x + "";
        }catch (Exception e){
            System.out.println("Values are not Integers");
            return null;
        }
    }



    public static void writeFile(String x, String y, String process){
        String path;

        String s1 = CPU.readMemory(x, process);
        if(s1 !=null){
            path= s1 +".txt";
        }else{
            if(x.length()>2&& x.charAt(0)=='_'&&x.charAt(1)==' '){
                x=x.substring(2);
            }
            path=x+".txt";
        }
        String data;
        String s2 = CPU.readMemory(y, process);
        if(s2 !=null){
            data= s2;
        }else{
            if(y.length()>2&& y.charAt(0)=='_'&&y.charAt(1)==' '){
                y=y.substring(2);
            }
            data=y;
        }

        try {
            File f=new File(path);
            FileWriter fw = new FileWriter(path);
            if(f.exists()) {
                FileReader fr = new FileReader(path);
                BufferedReader br = new BufferedReader(fr);
                String s = "";
                while (br.ready()) {
                    String line = br.readLine();
                    s += line;
                    s += "\n";
                }
                s+=data;
                s += "\n";
                fw.write(s);
                br.close();
            }else{
                fw.write(data);
            }
            fw.close();

        }catch(IOException e){

             System.out.println("Error in writing file");

        }
    }
    public static String readFile(String x, String process) {
        String path;
        String s1 = CPU.readMemory(x, process);
        if(s1 !=null){
            path= s1 +".txt";
        }else{
            if(x.length()>2&& x.charAt(0)=='_'&&x.charAt(1)==' '){
                x=x.substring(2);
            }
            path=x+".txt";
        }
        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            String s = "";
            while (br.ready()) {
                String line = br.readLine();
                s += line;
                s += "\n";
            }

            br.close();
            return s;
        } catch (IOException e) {
            return("Error in reading file");
        }
    }

    public static void print(String varName, String process){

        String s = CPU.readMemory(varName, process);
        if(s !=null){
            String varValue= s;
            System.out.println(varValue);

        }else{
            if(varName.length()>2&& varName.charAt(0)=='_'&&varName.charAt(1)==' '){
                varName=varName.substring(2);
            }
            System.out.println(varName);
        }

    }

//    private static void parse(String name) throws IOException {
//        String[] instArray=readProgram(name);
//        for (int i = 0; i < instArray.length; i++) {
//            interpret(instArray[i]);
//        }
//    }

//    public static void main(String[] args) throws IOException {
//        parse("Program 1.txt");
//        parse("Program 2.txt");
//        parse("Program 3.txt");
        //String n= interpret("print readFile input");
       // System.out.println(n);
//    }


}
