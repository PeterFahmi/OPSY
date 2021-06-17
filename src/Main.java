import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;

public class Main {

    private static HashMap<String, String> variables = new HashMap<String, String>();

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

    public static void interpret(String instruction) throws IOException {//print add readfile x readfile y
        Stack<String> stk=new Stack<String>();
        Stack<String> stk2=new Stack<String>();

        String[] split=instruction.split(" ");
        for (int i = 0; i < split.length; i++) {
            stk.push(split[i]);
        }
        while(stk.size()+stk2.size()>1){
            String cur=stk.pop();
            switch (cur){
                case "print":print(stk2.pop()); break;
                case "add":stk.push(add(stk2.pop(),stk2.pop()));break;
                case "assign":assign(stk2.pop(),stk2.pop()); break;
                case "readFile":stk.push(readFile(stk2.pop())); break;
                case "writeFile":writeFile(stk2.pop(),stk2.pop());break;
                case "input":stk.push(input());break;
                default:stk2.push(cur);
            }
        }

    }
    public static String interpret2(String instruction) {//print add readfile x readfile y
        Stack<String> stk=new Stack<String>(); //1) print c       2) add c input
        Stack<String> stk2=new Stack<String>();

        String[] split=instruction.split(" ");
        for (int i = 0; i < split.length; i++) {
            stk.push(split[i]);
        }
        boolean done=false;
        while(!done){
            String cur=stk.pop();
            switch (cur){
                case "print":print(stk2.pop());done=true; break;
                case "add":stk.push(add(stk2.pop(),stk2.pop()));done=true;break;
                case "assign":assign(stk2.pop(),stk2.pop());done=true; break;
                case "readFile":stk.push(readFile(stk2.pop()));done=true; break;
                case "writeFile":writeFile(stk2.pop(),stk2.pop());done=true;break;
                case "input":stk.push(input());done=true;break;
                default:stk2.push(cur);
            }
        }
        if(stk.size()+stk2.size()>1){
            String s="";
            while(!stk.isEmpty()){
                s=stk.pop()+" "+s;
            }
            while(!stk2.isEmpty()){
                s=s+stk2.pop()+" ";
            }
            s=s.substring(0,s.length()-1);
            return s;
        }else{
            return null;
        }

    }

    private static String input() {
        Scanner sc = new Scanner(System.in);
        return "_ "+sc.nextLine();
    }


    /**
     * This method assigns the input in the prompt to variable varName
     * @param varName
     */
    public static void assign(String varName,String value){
        if(readMemory(value)!=null){//checks if value is variable
            writeMemory(varName, readMemory(value));
        }else{
            if(value.length()>2&& value.charAt(0)=='_'&&value.charAt(1)==' '){
                value=value.substring(2);
            }
            writeMemory(varName, value);
        }
    }

    private static String readMemory(String varName){
        if(variables.containsKey(varName)){
            return variables.get(varName);
        }else{
            return null;
        }
    }

    private static void writeMemory(String varName,String value){
        variables.put(varName,value);
    }

    public static String add(String a, String b){
        int first;
        int second;
        try {
            first = Integer.parseInt(readMemory(a));


            if (readMemory(b) != null) {
                second = Integer.parseInt(readMemory(b));
            } else {
                if (b.length() > 2 && b.charAt(0) == '_' && b.charAt(1) == ' ') {
                    b = b.substring(2);
                }
                second = Integer.parseInt(b);
            }
            int x = first + second;
            writeMemory(a, x + "");
            return x + "";
        }catch (Exception e){
            System.out.println("Values are not Integers");
            return null;
        }
    }

    public static void writeFile(String x,String y){
        String path;

        if(readMemory(x)!=null){
            path=readMemory(x)+".txt";
        }else{
            if(x.length()>2&& x.charAt(0)=='_'&&x.charAt(1)==' '){
                x=x.substring(2);
            }
            path=x+".txt";
        }
        String data;
        if(readMemory(y)!=null){
            data=readMemory(y);
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
    public static String readFile(String x) {
        String path;
        if(readMemory(x)!=null){
            path=readMemory(x)+".txt";
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

    public static void print(String varName){

        if(readMemory(varName)!=null){
            String varValue=readMemory(varName);
            System.out.println(varValue);

        }else{
            if(varName.length()>2&& varName.charAt(0)=='_'&&varName.charAt(1)==' '){
                varName=varName.substring(2);
            }
            System.out.println(varName);
        }

    }

    private static void parse(String name) throws IOException {
        String[] instArray=readProgram(name);
        for (int i = 0; i < instArray.length; i++) {
            interpret(instArray[i]);
        }
    }

    public static void main(String[] args) throws IOException {
//        parse("Program 1.txt");
//        parse("Program 2.txt");
//        parse("Program 3.txt");
       String n= interpret2("print readFile _ text");
        System.out.println(n);
    }


}
