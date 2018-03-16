import java.util.*;
import javax.jms.*;
import java.io.*;
import java.lang.*;

public class TrainManagementSystem{
    
    public static String serverUrl;
    public static TrainMsgProducer breakdownMsger;
    
    public static void main(String[] args){
        Scanner sc = new Scanner(System.in);
        boolean run = true;
        serverUrl = "localhost"; //if this is done locally 
        initializeMsgProducers();
      
        
        while(run){
            System.out.println();
            System.out.println();
            

            System.out.println("====================================");
            System.out.println("       Train Management System      ");
            System.out.println("====================================");
            
            System.out.println("1. Update Server URL");
            System.out.println("2. Send Message");
            System.out.println("3. Check Messages");
            System.out.println("4. Quit Program");
            System.out.println();
            //cls();
            
            System.out.print("Option: ");
            int input = sc.nextInt();
            run = routeInput(input,sc);
            
            
            
        }

    }
    
    //Working
    public static boolean initializeMsgProducers(){
        breakdownMsger = new TrainMsgProducer(serverUrl,"q.breakdown",TrainMsgProducer.QUEUE);
        return true;
    }
    
    public static boolean routeInput(int input, Scanner sc){
        
        if(input == 1){
            updateURL(sc);
            cls();
            return true;
        }
        if(input == 2){
            messageScreen();
            cls();
            return true;
        }
        if (input == 3){
            cls();
            return true;
        }
        if(input == 4){
            cls();
            return false;
        }
        cls();
        return false;
        
    }
    
    public static void updateURL(Scanner sc){
        System.out.println(); 
        System.out.println("Current serverURL: " + serverUrl);
        System.out.print("New serverURL: ");
        System.out.flush();
        
        serverUrl = sc.next(); //Get the new serverURL and update it
        System.out.flush();
         
        //if you update reinitialize the message producers
        initializeMsgProducers();
    }
    
    public static void messageScreen(){
        processAllFiles(); //This process the XML files and send it 
    }
    
    public static void inboxScreen(){
        
    }
    
    public static void processAllFiles(){
        String address = "C:\\Users\\SLJ\\EI_Project\\TrainBreakDown\\msgResource";
        visitAllFiles(new File(address));
    }
    
    //Visit all the files in the directory and process them
    public static void visitAllFiles(File dir) {
      if (dir.isDirectory()) {
        String[] children = dir.list();
        if (children.length == 0) {
          System.out.println("There is no order to process.");
        }
            for (int i = 0; i < children.length; i++) {
              visitAllFiles(new File(dir, children[i]));
            }
      } else {
           readXML(dir);
        }
    }

    /*
     * This method reads an XML document and put the content into Vector data
     */
    public static void readXML(File filename) {
        
        BufferedReader bufferedReader = null;
        try {
            //Construct the BufferedReader object
            bufferedReader = new BufferedReader(new FileReader(filename));
            String line = null;
            Vector data = new Vector();
            while ((line = bufferedReader.readLine()) != null) {
                //Add the line from XML file to the message to be sent as JMS msg
                data.addElement(line);
            }
          sendMessage(data);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            //Close the BufferedReader
            try {
                if (bufferedReader != null)
                    bufferedReader.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
  }
    
    public static void sendMessage(Vector data){
        
        StringBuffer finalMessage = new StringBuffer();
        int i;
        if(data.size() == 0){
            System.err.println("***Error: Order file is empty\n");
        }else{
            finalMessage.append((String)data.elementAt(0));
            for(i = 1; i < data.size();i++){
                finalMessage.append(' ');
                finalMessage.append((String)data.elementAt(i));
            }
        }
        
        breakdownMsger. sendMessage(finalMessage.toString());
        
        
    }
    
    public static void cls(){
        for (int i = 0 ; i < 20; i ++){
            System.out.println();
        }
       
    }
}
