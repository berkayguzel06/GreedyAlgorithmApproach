import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Greedy {
    static int year = 29;  //(n) how many years that will be calculated    
    static int promotedPlayers = 5; //(p) holds the how many player can be promoted   
    static int hire = 10;  //(c) money of hiring a player   
    static int[] yearDemand = new int[50];   // holds the demand of each year
    static int[] yearSalary = new int[311];   // holds the salary in each year
    public static void main(String[] args) throws Exception {
        readTXT(); // read data from txt
        long start = System.currentTimeMillis();// start time
        greedy(0,0,0); // calculate the best option using greedy algorihtm
        System.out.print("Time: ");
        System.out.println(System.currentTimeMillis()-start);// calculate the end time
    }
    static public void greedy(int currentYear, int players, int money){
        if(year==currentYear){// if this year not wanted return
            return;
        }
        currentYear+=1;// increase current year
        int currentMoney = money;
        int demand = yearDemand[currentYear-1]; // takes the current demand
        int nextYearDemand; // calculate the next year demand for the best option
        if(year>=currentYear+1){
            nextYearDemand = yearDemand[currentYear];
        }else{
            nextYearDemand = 0;
        }
        if(demand>=players+promotedPlayers){// if the demand is higher then current players
            currentMoney+=(demand-(players+promotedPlayers))*hire;// add hire cost
            printPath(currentYear,currentMoney,demand-(players+promotedPlayers),demand,0,0);// prints the choosen way
            greedy(currentYear, 0, currentMoney);// next year
        }else{
            int minMoney = 9999999;// contains the best option money
            int minPlayer = 0;// contains the best option player number
            for (int i = 1; i <= promotedPlayers; i++) { // this loop scans the all possible ways 
                int tempMoney = currentMoney;
                int p = (players+i)-demand;
                if(p>=0&&i!=promotedPlayers+1){// if our players can satisfied this year
                    if(p!=0)// if we retain players in this year add the salary money
                        tempMoney+=yearSalary[p];
                    int nextP = (p+promotedPlayers)-nextYearDemand;// calculate the next year demand
                    if(nextP<0){
                        tempMoney+=(nextP*(-1))*hire;// calculate the next year money
                    }
                    if(minMoney>tempMoney){// if minmoney higher than this option
                        minMoney=tempMoney;
                        minPlayer=p;
                    }
                }
                if(i==promotedPlayers){// if we see all possible ways in this year
                    if(minPlayer!=0){
                        currentMoney+=yearSalary[minPlayer-1];// if we have player add the salary
                    }
                    if(minPlayer!=0){
                        printPath(currentYear,currentMoney,0,demand,yearSalary[minPlayer-1],minPlayer);// prints the choosen way
                    }else{
                        printPath(currentYear,currentMoney,0,demand,0,minPlayer);// prints the choosen way
                    }
                    greedy(currentYear, minPlayer, currentMoney);// next year
                }
            }
        }
    }
    static public void printPath(int currentYear, int money, int hired, int demand, int salary, int retainedPlayers){//prints the current way
        int[] location = new int[6];
        location[0]=currentYear;
        location[1]=money;
        location[2]=hired;
        location[3]=demand;
        location[4]=salary;
        location[5]=retainedPlayers;
        String[] s = {"Year: ","Current Cost: ","Hired Player: ","Demand: ","Given Salary: ","Retained Player Number: "};
        for (int i = 0; i < s.length; i++) {
            System.out.print(String.format(" %s%d,",s[i], location[i]));  
        }
        System.out.println();
    }
    static public void readTXT() throws FileNotFoundException{// read txt's and store data
        String salaryTxt = "players_salary.txt";
        String playerDemand = "yearly_player_demand.txt";
        File salaryFile = new File(salaryTxt);
        File demandFile = new File(playerDemand);
        Scanner s = new Scanner(salaryFile);
        Scanner d = new Scanner(demandFile);
        int ind = 0;
        s.nextLine();
        while(s.hasNextLine()){
            String[] get = s.nextLine().split("	");
            yearSalary[ind] = Integer.parseInt(get[1]);
            ind+=1;
        }
        ind = 0;
        d.nextLine();
        while(d.hasNextLine()){
            String[] get = d.nextLine().split("	");
            yearDemand[ind] = Integer.parseInt(get[1]);
            ind+=1;
        }
        d.close();
        s.close();
    }
}