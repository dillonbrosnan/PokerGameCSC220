package PJ4;

import java.util.*;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * Ref: http://en.wikipedia.org/wiki/Video_poker
 *      http://www.google.com/ig/directory?type=gadgets&url=www.labpixies.com/campaigns/videopoker/videopoker.xml
 *
 *
 * Short Description and Poker rules:
 *
 * Video poker is also known as draw poker. 
 * The dealer uses a 52-card deck, which is played fresh after each playerHand. 
 * The player is dealt one five-card poker playerHand. 
 * After the first draw, which is automatic, you may hold any of the cards and draw 
 * again to replace the cards that you haven't chosen to hold. 
 * Your cards are compared to a table of winning combinations. 
 * The object is to get the best possible combination so that you earn the highest 
 * payout on the bet you placed. 
 *
 * Winning Combinations
 *  
 * 1. Jacks or Better: a pair pays out only if the cards in the pair are Jacks, 
 * 	Queens, Kings, or Aces. Lower pairs do not pay out. 
 * 2. Two Pair: two sets of pairs of the same card denomination. 
 * 3. Three of a Kind: three cards of the same denomination. 
 * 4. Straight: five consecutive denomination cards of different suit. 
 * 5. Flush: five non-consecutive denomination cards of the same suit. 
 * 6. Full House: a set of three cards of the same denomination plus 
 * 	a set of two cards of the same denomination. 
 * 7. Four of a kind: four cards of the same denomination. 
 * 8. Straight Flush: five consecutive denomination cards of the same suit. 
 * 9. Royal Flush: five consecutive denomination cards of the same suit, 
 * 	starting from 10 and ending with an ace
 *
 */
/* This is the main poker game class.
 * It uses Deck and Card objects to implement poker game.
 * Please do not modify any data fields or defined methods
 * You may add new data fields and methods
 * Note: You must implement defined methods
 */
public class PokerGame {

    // default constant values
    private static final int startingBalance = 100;
    private static final int numberOfCards = 5;

    // default constant payout value and playerHand types
    private static final int[] multipliers = {1, 2, 3, 5, 6, 9, 25, 50, 250};
    private static final String[] goodHandTypes = {
        "Royal Pair", "Two Pairs", "Three of a Kind", "Straight", "Flush	",
        "Full House", "Four of a Kind", "Straight Flush", "Royal Flush"};

    // must use only one deck
    private static final Deck oneDeck = new Deck(1);

    // holding current poker 5-card hand, balance, bet    
    private List<Card> playerHand;
    private List<Card> tempHand = new ArrayList<Card>(5);
    private int playerBalance;
    private int playerBet;

    /**
     * default constructor, set balance = startingBalance
     */
    public PokerGame() {
        this(startingBalance);
    }

    /**
     * constructor, set given balance
     */
    public PokerGame(int balance) {
        this.playerBalance = balance;
    }

    /**
     * This display the payout table based on multipliers and goodHandTypes
     * arrays
     */
    private void showPayoutTable() {
        System.out.println("\n\n");
        System.out.println("Payout Table   	      Multiplier   ");
        System.out.println("=======================================");
        int size = multipliers.length;
        for (int i = size - 1; i >= 0; i--) {
            System.out.println(goodHandTypes[i] + "\t|\t" + multipliers[i]);
        }
        System.out.println("\n\n");
    }

    /**
     * Check current playerHand using multipliers and goodHandTypes arrays Must
     * print yourHandType (default is "Sorry, you lost") at the end of function.
     * This can be checked by testCheckHands() and main() method.
     */
    private void checkHands() {
        /**
         * This below creates a temporary hand to be sorted, it is much easier
         * to determine what kind of hand you are holding if the cards are
         * sorted.
         */
        tempHand.removeAll(tempHand);
        for (int i = 0; i < playerHand.size(); i++) {
            tempHand.add(playerHand.get(i));
        }
        System.out.println(tempHand);
        Collections.sort(tempHand, new Comparator<Card>() {

            @Override
            public int compare(Card t, Card t1) {
                int r1 = t.getRank();
                int r2 = t1.getRank();
                if (r1 > r2) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });
        /**
         * These boolean statements below call different methods, and return a
         * the value of the position of the multipliers in the multiplier array
         * and the position of the goodhandType array
         */
        int x = 0;
        if (royalFlush()) {
            x = 8;
        } else if (straightFlush()) {
            x = 7;
        } else if (flush()) {
            x = 4;
        } else if (straight()) {
            x = 3;
        } else if (fullHouse()) {
            x = 5;
        } else if (fourOfAKind()) {
            x = 6;
        } else if (threeOfAKind()) {
            x = 2;
        } else if (twoPair()) {
            x = 1;
        } else if (jacksOrBetter()) {
            x = 0;
        } else {
            x = -1;
        }
        /*
         if you lost, x = -1, as in none of the above statments are true, if not
         i print the type of hand from the array, and update the playerBalance
         */
        if (x == -1) {
            System.out.println("Sorry, you lost.");
        } else {
            System.out.println(goodHandTypes[x]);
            int mult = multipliers[x];
            playerBalance += (playerBet * mult);
        }

        // implement this method!
    }
    /*
     Checks if jacks or better but moving through the tempHand and at the first 
     sign of a pair of jacks or better, breaks from the iterator, and returns a true
     value
     */

    private boolean jacksOrBetter() {
        boolean ret = false;
        for (int i = 0; i < this.tempHand.size() - 1; i++) {
            if (tempHand.get(i).getRank() == playerHand.get(i + 1).getRank()) {
                if (tempHand.get(i).getRank() >= 11) {
                    ret = true;
                    break;
                } else {
                    ret = false;
                }
            }
        }
        return ret;
    }

    /**
     * Counts how many pairs are in the tempHand, if there are 2, we return true
     * iterates through temp hand and counts how many pairs it contains
     *
     * @return weather there are 2 pairs or not
     */
    private boolean twoPair() {
        int count = 0;
        boolean ret = false;
        for (int i = 0; i < tempHand.size() - 1;) {
            if (tempHand.get(i).getRank() == tempHand.get(i + 1).getRank()) {
                count++;
                i += 2;
            } else {
                i++;
            }
        }
        if (count == 2) {
            ret = true;
        } else {
            ret = false;
        }
        return ret;
    }

    /**
     * method speifically for threeofAKind() counts how many of the same rank
     * are in the hand
     *
     * @return
     */
    private int threeOfAKindCheck() {
        boolean ret = false;
        int count = 1;
        int rank = 0;
        for (int i = 0; i < tempHand.size() - 1; i++) {
            if (tempHand.get(i).getRank() != tempHand.get(i + 1).getRank()) {
                count = 1;
            } else {
                count++;
                rank = tempHand.get(i).getRank();
            }
        }
        if (count == 3) {
            return rank;
        } else {
            return -1;
        }
    }
    /*
     returns true if threeOfAKindCheck() returns anything but -1
     */

    private boolean threeOfAKind() {
        return threeOfAKindCheck() != -1;
    }

    /**
     * in a sorted hand, if there is 4 of a kind, either the first and fourth
     * card are the same rank, or the second and last card are the same rank,
     * this checks and returns true if that is true
     */
    private boolean fourOfAKind() {
        if (tempHand.get(0).getRank() == tempHand.get(3).getRank() || tempHand.get(1).getRank() == tempHand.get(4).getRank()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * for a full house, in a sorted hand, either the first and second card are
     * the same rank and the third and last card are the same rank, but the
     * first and third are not the same rank, or the first and third are the
     * same rank the fourth and fifth card are the same rank, but the first and
     * fourth are not the same rank
     *
     * @return true if above is true
     */
    private boolean fullHouse() {
        if (tempHand.get(0).getRank() == tempHand.get(1).getRank() && tempHand.get(2).getRank() == tempHand.get(4).getRank() && tempHand.get(0).getRank() != tempHand.get(4).getRank()) {
            return true;
        } else if (tempHand.get(0).getRank() == tempHand.get(2).getRank() && tempHand.get(3).getRank() == tempHand.get(4).getRank() && tempHand.get(0).getRank() != tempHand.get(4).getRank()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * iterates through hand, checks if rank of first card = rank+1 of second
     * card, and continues this through hand
     *
     * @return true if statment above is true
     */
    private boolean straight() {
        boolean ret = true;
        if (tempHand.get(0).getRank() == 1 && tempHand.get(1).getRank() != 2) {
            for (int i = 1; i < tempHand.size() - 1; i++) {
                if (tempHand.get(i).getRank() + 1 == tempHand.get(i + 1).getRank()) {
                    ret = true;
                } else {
                    ret = false;
                    break;
                }
            }
        } else {
            for (int i = 0; i < tempHand.size() - 1; i++) {
                if (tempHand.get(i).getRank() + 1 == tempHand.get(i + 1).getRank()) {
                    ret = true;
                } else {
                    ret = false;
                    break;
                }
            }
        }
        return ret;
    }

    /**
     * iterates through hand and checks if suit is the same
     *
     * @return
     */
    private boolean flush() {
        boolean ret = true;
        for (int i = 0; i < tempHand.size() - 1; i++) {
            if (tempHand.get(i).getSuit() == tempHand.get(i + 1).getSuit()) {
                ret = true;
            } else {
                ret = false;
                break;
            }
        }
        return ret;
    }

    private boolean straightFlush() {
        if (flush() && straight()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean royalFlush() {
        if (straightFlush() && tempHand.get(0).getRank() == 1) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * ***********************************************
     * add new private methods here ....
     *
     ************************************************
     */
    
    /**
     * prints the current hand
     */
    private void showHand() {
        System.out.println(this.playerHand);
    }
    /**
    *prints current balance 
    */
    private void checkBalance() {
        System.out.println("Current balance: $" + this.playerBalance);
    }

    /**
     * okay, so it get pretty confusing here, but it works
     */
    public void play() {
        //String keep;
        boolean foo1 = false;
        boolean foo2 = false;
        boolean foo3;
        boolean foo4 = true;
        char array[] = new char[5];
        //int throArray[] = {1,2,3,4,5};
        //int keepArray[] = new int[5];
        //int throArray2[] = new int[5];
        int z = 0;
        Scanner input = new Scanner(System.in); //set up scanner
        showPayoutTable();
        System.out.println("Current Balance: $" + playerBalance); //display curent balancew
        do {
            /*throArray: create an array of int 1-5 to keep track of what we are goint to 
            throw away later, will be modified later
            keepArray: created empty array for what we are going to keep
            throArray2: used for storing original throArray
             */
            int throArray[] = {1, 2, 3, 4, 5}; 
            int keepArray[] = new int[5];     
            int throArray2[] = new int[5];     
            System.out.println("Would you like to play a new game? yes/no");
            String answer = input.nextLine();
            Deck deck = new Deck(1);//create one deck
            //Accounts for errors in entry
            do {
                if (answer.equalsIgnoreCase("yes") || answer.equalsIgnoreCase("no")) {
                    foo1 = false;
                } else {
                    System.out.println("Please enter in ''Yes'' or ''No'' only");
                    foo1 = true;
                    answer = input.nextLine();
                }
            } while (foo1);
            if (answer.equalsIgnoreCase("yes")) {
                deck.reset();
                deck.shuffle();
                playerHand = new ArrayList<Card>(5);//create playerHand list
                do {
                    System.out.println("What would you like to bet?");
                    try {
                        playerBet = input.nextInt();
                        /*
                        checks if user has enough money for bet and accounts for
                        input error
                        */
                        if (playerBet > playerBalance) {
                            System.out.println("You do not have enough money to place that bet. Please enter a smaller ammout");
                            foo2 = true;
                        } else {
                            foo2 = false;
                        }
                    } catch (InputMismatchException e) {
                        System.out.println("Please enter whole integers only");
                        input.next();
                        foo2 = true;
                    }
                } while (foo2);
                playerBalance -= playerBet; //update player balance
                checkBalance();
                try {
                    playerHand = deck.deal(5); //fills player hand
                } catch (PlayingCardException ex) {
                    Logger.getLogger(PokerGame.class.getName()).log(Level.SEVERE, null, ex);
                }
                showHand();
                String throString;
                List<String> keep;
                /*
                creates a list to store the numbers of what to keep as strings
                */
                List<String> thro = new ArrayList<String>(5); 
                for (int i = 0; i < 5; i++) {
                    int x = i + 1;
                    String add = Integer.toString(x);
                    thro.add(add);
                }

                List<Card> replacement = new ArrayList<Card>();
                int keepsize = 0;
                int check;
                foo3 = false;
                System.out.println("Which cards woulld you like to keep?");
                String dummy = input.nextLine();
                //treat throString as keepString, it is a string of what user wants
                //to keep
                throString = input.nextLine();
                if (!throString.equals("")) {
                    do {
                        /**
                         * accounts for error in how many cards to keep
                         */
                        keep = Arrays.asList(throString.split(" "));
                        check = keep.size();
                        for (int i = 0; i <= keepsize; i++) {
                            try {
                                /**
                                 * checks that numbers entered are ints 
                                 */
                                check = Integer.parseInt(keep.get(i)); 
                                if (check >= 0 && check <= numberOfCards) {
                                    foo3 = false;
                                } else {
                                    foo3 = true;
                                    System.out.println("Invalid Input please enter numbers between 0 and 5" + numberOfCards);
                                    throString = input.next();
                                }
                            } catch (NumberFormatException e) {
                                foo3 = true;
                                System.out.println("Invalid input. please enter single integers only followed by a space");
                                throString = input.nextLine();
                            }
                        }
                    } while (foo3);
                    /**
                     * stores strings from keepList into keepArray as ints
                     */
                    for (int i = 0; i < keep.size(); i++) {
                        keepArray[i] = Integer.parseInt(keep.get(i));
                    }
                    Arrays.sort(keepArray); //sorts array
                    /**
                     * if number in throArray is also in keepArray, we set throArray
                     * index of that number to 0
                     */
                    for (int i = 0; i < throArray.length; i++) {
                        for (int j = 0; j < keepArray.length; j++) {
                            if (throArray[i] == keepArray[j]) {
                                throArray[i] = 0;
                            }
                        }
                    }
                    //sets throArray2 the same as throArray
                    for (int i = 0; i < throArray.length; i++) {
                        throArray2[i] = throArray[i];
                    }/**
                     * removes cards in playerhand specified in throArray
                     * int z is used to keep track of how many cards thrown away
                     * when a card is thrown away, every other number in throArray
                     * is lowered by 1, as the list has shifted
                     */
                    for (int i = 0; i < throArray.length; i++) {
                        int x = 1;
                        if (throArray[i] > 0) {
                            playerHand.remove(throArray[i] - 1);
                            z++;
                            for (int j = 0; j < throArray.length; j++) {
                                throArray[j] -= 1;
                            }
                        }
                    }
                    try {
                        replacement = deck.deal(z); //deals a list into replacment lsit
                    } catch (PlayingCardException ex) {
                        Logger.getLogger(PokerGame.class.getName()).log(Level.SEVERE, null, ex);
                    } /**
                     * traverses throArray2, and if number is not 0, replace the card
                     * with a card taken from replacment list
                     */
                    for (int i = 0; i < throArray2.length; i++) {
                        if (throArray2[i] != 0) {
                            Card temp = replacement.remove(0);
                            playerHand.add(throArray2[i] - 1, temp);
                        }
                    }

                } else { //below is if user wants every card replaced
                    try {
                        replacement = deck.deal(5);
                    } catch (PlayingCardException ex) {
                        Logger.getLogger(PokerGame.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    for (int i = 0; i < playerHand.size(); i++) {
                        playerHand.remove(0);
                    }
                    for (int i = 0; i < replacement.size(); i++) {
                        playerHand.add(replacement.remove(0));
                    }
                }


                checkHands(); //checks hands for winning pair
                checkBalance(); //display balance
                if (playerBalance > 0) {
                    for (int i = 0; i < keepArray.length; i++) {
                        throArray[i] = i + 1;
                    }
                    foo4 = true;
                } else {
                    System.out.println("Not enough money to continue.");
                    foo4 = false;
                }

            } else {
                foo4 = false;
            }
        } while (foo4);

        /**
         * The main algorithm for single player poker game
         *
         * Steps: showPayoutTable()
         *
         * ++ show balance, get bet verify bet value, update balance reset deck,
         * shuffle deck, deal cards and display cards ask for positions of cards
         * to keep get positions in one input line update cards check hands,
         * display proper messages update balance if there is a payout if
         * balance = O: end of program else ask if the player wants to play a
         * new game if the answer is "no" : end of program else :
         * showPayoutTable() if user wants to see it goto ++
         */
        // implement this method!
    }

    /**
     * ***********************************************
     * Do not modify methods below
     * /*************************************************
     *
     *
     * /** testCheckHands() is used to test checkHands() method checkHands()
     * should print your current hand type
     */
    public void testCheckHands() {
        try {
            playerHand = new ArrayList<Card>();

            // set Royal Flush
            playerHand.add(new Card(1, 4));
            playerHand.add(new Card(10, 4));
            playerHand.add(new Card(12, 4));
            playerHand.add(new Card(11, 4));
            playerHand.add(new Card(13, 4));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Straight Flush
            playerHand.set(0, new Card(9, 4));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");
            // set Straight
            playerHand.set(4, new Card(8, 2));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");
            // set Flush 
            playerHand.set(4, new Card(5, 4));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

		// "Royal Pair" , "Two Pairs" , "Three of a Kind", "Straight", "Flush	", 
            // "Full House", "Four of a Kind", "Straight Flush", "Royal Flush" };
            // set Four of a Kind
            playerHand.clear();
            playerHand.add(new Card(8, 4));
            playerHand.add(new Card(8, 1));
            playerHand.add(new Card(12, 4));
            playerHand.add(new Card(8, 2));
            playerHand.add(new Card(8, 3));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Three of a Kind
            playerHand.set(4, new Card(11, 4));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Full House
            playerHand.set(2, new Card(11, 2));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Two Pairs
            playerHand.set(1, new Card(9, 2));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // set Royal Pair
            playerHand.set(0, new Card(3, 2));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");

            // non Royal Pair
            playerHand.set(2, new Card(3, 4));
            System.out.println(playerHand);
            checkHands();
            System.out.println("-----------------------------------");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /* Quick testCheckHands() */
    public static void main(String args[]) {
        PokerGame pokergame = new PokerGame();
        //pokergame.testCheckHands();
        pokergame.play();
    }
}
