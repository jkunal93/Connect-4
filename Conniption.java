import java.util.*;
import java.io.*;

public class Conniption{
	
	private Board b;
    private Scanner input;
    private int nextMove=-1;
    private int maxDepth = 9;
    //private int maxFlips = 4;
	
    public static void main(String args[]) throws IOException{
    	
    	//FileWriter fw = new FileWriter("test.txt"); 
    	//BufferedWriter bw = new BufferedWriter(fw);
    	Board b = new Board();
    	//Board fb = new Board();
    	Conniption cp = new Conniption(b);  
        cp.AIagent();
    	
    }
    
    public Conniption(Board b){
        this.b = b;
        input = new Scanner(System.in);
    }
    
    public void AIagent(){
    	
        Scanner scan = new Scanner(System.in);
        System.out.println("Would you like to play first? (yes/no) ");
        String answer = scan.next().trim();
        
       if(answer.equalsIgnoreCase("yes")) playerMove();
        b.display();
        b.diskEntry(3, 'X');
        b.display();
        
        while(true){ 
        	playerMove();
            b.display();
            
            int gameResult = winCheck(b);
            if(gameResult==1){System.out.println("AI Wins!");break;}
            else if(gameResult==2){System.out.println("You Win!");break;}
            else if(gameResult==0){System.out.println("Draw!");break;}
            
            b.diskEntry(getAIMove(), 'X');
            b.display();
            gameResult = winCheck(b);
            if(gameResult==1){System.out.println("AI Wins!");break;}
            else if(gameResult==2){System.out.println("You Win!");break;}
            else if(gameResult==0){System.out.println("Draw!");break;}
        }
    	
    }
    
    
    public void playerMove(){
    	
        System.out.println("Player's turn (1-7): ");
        int move = input.nextInt();
        while(move<1 || move > 7 || !b.validMove(move-1)){
            System.out.println("Invalid move.\n\nPlayer's turn (1-7): "); 
            move = input.nextInt();
        }
        
        //Assume 2 is the opponent
        b.diskEntry(move-1, 'O'); 
    }
    
    //Game Result
    public int winCheck(Board b){
    	
        int aiScore = 0, playerScore = 0;
        for(int i=5;i>=0;--i){
            for(int j=0;j<=6;++j){
                if(b.board[i][j]==' ') continue;
                
                //Calculate to the right
                if(j<=3){
                    for(int k=0;k<4;++k){
                            if(b.board[i][j+k]=='X') aiScore++;
                            else if(b.board[i][j+k]=='O') playerScore++;
                            else break; 
                    }
                    if(aiScore==4)return 1; else if (playerScore==4)return 2;
                    aiScore = 0; playerScore = 0;
                } 
                
                //Calculate Upwards
                if(i>=3){
                    for(int k=0;k<4;++k){
                            if(b.board[i-k][j]=='X') aiScore++;
                            else if(b.board[i-k][j]=='O') playerScore++;
                            else break;
                    }
                    if(aiScore==4)return 1; else if (playerScore==4)return 2;
                    aiScore = 0; playerScore = 0;
                } 
                
                //Calculate diagonally up-right
                if(j<=3 && i>= 3){
                    for(int k=0;k<4;++k){
                        if(b.board[i-k][j+k]=='X') aiScore++;
                        else if(b.board[i-k][j+k]=='O') playerScore++;
                        else break;
                    }
                    if(aiScore==4)return 1; else if (playerScore==4)return 2;
                    aiScore = 0; playerScore = 0;
                }
                
                //Calculating diagonally up-left
                if(j>=3 && i>=3){
                    for(int k=0;k<4;++k){
                        if(b.board[i-k][j-k]=='X') aiScore++;
                        else if(b.board[i-k][j-k]=='O') playerScore++;
                        else break;
                    } 
                    if(aiScore==4)return 1; else if (playerScore==4)return 2;
                    aiScore = 0; playerScore = 0;
                }  
            }
        }
        
        for(int j=0;j<7;++j){
            //Game continues
            if(b.board[0][j]==' ')return -1;
        }
        //Game draw!
        return 0;
    }
    
    public int getAIMove(){
        nextMove = -1;
        calculate(0, 'X', Integer.MIN_VALUE, Integer.MAX_VALUE);
        return nextMove;
    }
    
  //Evaluate board favorableness for AI
    public int evaluate(Board b){
      
        int aiScore=1;
        int score=0;
        int blanks = 0;
        int k=0, moreMoves=0;
        for(int i=5;i>=0;--i){
            for(int j=0;j<=6;++j){
                
                if(b.board[i][j]==' ' || b.board[i][j]=='O') continue; 
                // Left 4 columns
                if(j<=3){
                    for(k=1;k<4;++k){
                        if(b.board[i][j+k]=='X')aiScore++;
                        else if(b.board[i][j+k]=='O'){aiScore=0;blanks = 0;break;}
                        else blanks++;
                    }
                     
                    moreMoves = 0; 
                    if(blanks>0){
                        for(int c=1;c<4;++c){
                            int column = j+c;
                            for(int m=i; m<= 5;m++){
                             if(b.board[m][column]==' ')moreMoves++;
                                else break;
                            } 
                        } 
                    }
                    
                    if(moreMoves!=0) score += scoreCount(aiScore, moreMoves);
                    aiScore=1;   
                    blanks = 0;
                }
                //3 rows from bottom
                if(i>=3){
                    for(k=1;k<4;++k){
                        if(b.board[i-k][j]=='X')aiScore++;
                        else if(b.board[i-k][j]=='O'){aiScore=0;break;} 
                    } 
                    moreMoves = 0; 
                    
                    if(aiScore>0){
                        int column = j;
                        for(int m=i-k+1; m<=i-1;m++){
                         if(b.board[m][column]==' ')moreMoves++;
                            else break;
                        }  
                    }
                    if(moreMoves!=0) score += scoreCount(aiScore, moreMoves);
                    aiScore=1;  
                    blanks = 0;
                }
                // Right 4 columns 
                if(j>=3){
                    for(k=1;k<4;++k){
                        if(b.board[i][j-k]=='X')aiScore++;
                        else if(b.board[i][j-k]=='O'){aiScore=0; blanks=0;break;}
                        else blanks++;
                    }
                    moreMoves=0;
                    if(blanks>0) 
                        for(int c=1;c<4;++c){
                            int column = j- c;
                            for(int m=i; m<= 5;m++){
                             if(b.board[m][column]==' ')moreMoves++;
                                else break;
                            } 
                        } 
                    
                    if(moreMoves!=0) score += scoreCount(aiScore, moreMoves);
                    aiScore=1; 
                    blanks = 0;
                }
                // left-bottom part 
                if(j<=3 && i>=3){
                    for(k=1;k<4;++k){
                        if(b.board[i-k][j+k]=='X')aiScore++;
                        else if(b.board[i-k][j+k]=='O'){aiScore=0;blanks=0;break;}
                        else blanks++;                        
                    }
                    moreMoves=0;
                    if(blanks>0){
                        for(int c=1;c<4;++c){
                            int column = j+c, row = i-c;
                            for(int m=row;m<=5;++m){
                                if(b.board[m][column]==' ')moreMoves++;
                                else if(b.board[m][column]=='X');
                                else break;
                            }
                        } 
                        if(moreMoves!=0) score += scoreCount(aiScore, moreMoves);
                        aiScore=1;
                        blanks = 0;
                    }
                }
                // right-bottom part
                if(i>=3 && j>=3){
                    for(k=1;k<4;++k){
                        if(b.board[i-k][j-k]=='X')aiScore++;
                        else if(b.board[i-k][j-k]=='O'){aiScore=0;blanks=0;break;}
                        else blanks++;                        
                    }
                    moreMoves=0;
                    if(blanks>0){
                        for(int c=1;c<4;++c){
                            int column = j-c, row = i-c;
                            for(int m=row;m<=5;++m){
                                if(b.board[m][column]==' ')moreMoves++;
                                else if(b.board[m][column]=='X');
                                else break;
                            }
                        } 
                        if(moreMoves!=0) score += scoreCount(aiScore, moreMoves);
                        aiScore=1;
                        blanks = 0;
                    }
                } 
            }
        }
        return score;
    }
    
    int scoreCount(int aiScore, int moreMoves){   
        int moveScore = 4 - moreMoves;
        if(aiScore==0)return 0;
        else if(aiScore==1)return 1*moveScore;
        else if(aiScore==2)return 10*moveScore;
        else if(aiScore==3)return 100*moveScore;
        else return 1000;
    }
    
    public int calculate(int depth, char turn, int min, int max){
        
        if(max<=min){
        	if(turn == 'X'){
        		return Integer.MAX_VALUE; 
        	}else{
        		return Integer.MIN_VALUE; 
        	}
        }
        int gameResult = winCheck(b);
        
        if(gameResult==1){ 
        	return Integer.MAX_VALUE/2;
        }else if(gameResult==2){
        	return Integer.MIN_VALUE/2; 
        }else if(gameResult==0){ 
        	return 0; 
        } 
        
        if(depth==maxDepth){
        	return evaluate(b);
        }
        
        int maxScore=Integer.MIN_VALUE, minScore = Integer.MAX_VALUE;
                
        for(int j=0;j<=6;++j){
            
            int currentScore = 0;
            
            if(!b.validMove(j)){
            	continue;
            } 
            
            if(turn=='X'){
            		//System.out.println("At depth "+depth+" column entered in "+(j+1)+" for "+turn);
                    b.diskEntry(j, 'X');
                    //System.out.println("For X");
                    //b.display();
                    currentScore = calculate(depth+1, 'O', min, max);
                    
                    if(depth==0){
                        //System.out.println("Score for location "+j+" = "+currentScore);
                        if(currentScore > maxScore)nextMove = j; 
                        if(currentScore == Integer.MAX_VALUE/2){b.undo(j);break;}
                    }
                    
                    maxScore = Math.max(currentScore, maxScore);
                    min = Math.max(currentScore, min);  
            
            }else if(turn=='O'){
            		//System.out.println("At depth "+depth+" column entered in "+(j+1)+" for "+turn);
                    b.diskEntry(j, 'O');
                    //System.out.println("For O");
                    //b.display();
                    currentScore = calculate(depth+1, 'X', min, max);
                    minScore = Math.min(currentScore, minScore);
                    max = Math.min(currentScore, max); 
            }  
            b.undo(j); 
            if(currentScore == Integer.MAX_VALUE || currentScore == Integer.MIN_VALUE) break; 
        }
        
        return turn=='X'?maxScore:minScore;
    }
    
}

class Board{
	
	char board[][] = new char[6][7];
	//char b1[][] = new char[6][7];
	//char fb[][] = new char[6][7];
	
	public Board(){
		
		board = new char[][]{
            {' ',' ',' ',' ',' ',' ',' ',},
            {' ',' ',' ',' ',' ',' ',' ',},
            {' ',' ',' ',' ',' ',' ',' ',},
            {' ',' ',' ',' ',' ',' ',' ',},
            {' ',' ',' ',' ',' ',' ',' ',},
            {' ',' ',' ',' ',' ',' ',' ',},    
        };
        
        /*b1 = new char[][]{
            {' ',' ',' ',' ',' ',' ',' ',},
            {' ',' ',' ',' ',' ',' ',' ',},
            {' ',' ',' ',' ',' ',' ',' ',},
            {' ',' ',' ','O','O',' ',' ',},
            {' ','O','X','X','X',' ',' ',},
            {'O','X','O','O','X','O',' ',},    
        };
        
        fb = flip(b1);
        displayflip();*/
        
		
	}
	
	/*public void displayflip(){
		
		System.out.println();
        for(int i=0;i<=5;++i){
            for(int j=0;j<=6;++j){
                System.out.print(" "+fb[i][j]+" ");
            }
            System.out.println(i+1);
        }
        System.out.println(" 1  2  3  4  5  6  7");
		
	}*/
	
	public void display(){
		
		System.out.println();
        for(int i=0;i<=5;++i){
            for(int j=0;j<=6;++j){
                System.out.print(" "+board[i][j]+" ");
            }
            System.out.println(i+1);
        }
        System.out.println(" 1  2  3  4  5  6  7");
		
	}
	
	public char[][] flip(char b[][]){
    	char fb[][] = new char[6][7];
    	
    	for(int i=0;i<=6;i++){
        	int a=5;
        	for(int j=0;j<=5;j++){
        		
        		if(b[j][i]!=' '){
        			
        			fb[a][i]=b[j][i];
        			a--;
        		}else{
        			fb[j][i]=b[j][i];
        		}
        	}
        	
        }
    	
    	return fb;
    }
	
	public boolean validMove(int column){
        return board[0][column]==' ';
    }
	
	public boolean diskEntry(int column, char player){
		
		if(!validMove(column)) {System.out.println("Illegal move!"); return false;}
        for(int i=5;i>=0;--i){
            if(board[i][column] == ' ') {
                board[i][column] = player;
                return true;
            }
        }
        return false;
		
	}
	
	public void undo(int column){
        for(int i=0;i<=5;++i){
            if(board[i][column] != ' ') {
                board[i][column] = ' ';
                break;
            }
        }        
    }
	
}