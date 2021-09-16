package Bonis;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import lombok.Data;

@Data
public class Deck {
	
	public List<Case> cases;
	
    public Deck() {
    	cases =new  ArrayList<Case>();
    	cases.add(new Case (1,0, 0));
    	cases.add(new Case (2,1, 5));
    	cases.add(new Case (3,1, 10));
    	cases.add(new Case (4,2, 5));
    	cases.add(new Case (5,2, 10));
    	cases.add(new Case (6,1, 25));
    	cases.add(new Case (7,1, 50));
    	cases.add(new Case (8,2, 25));
    	// shuffleArray();
    }
    
public  void shuffleArray()
    {
      // If running on Java 6 or older, use `new Random()` on RHS here
      Random rnd = ThreadLocalRandom.current();
      for (int i = cases.size() - 1; i > 0; i--)
      {
        int index = rnd.nextInt(i + 1);
        // Simple swap
        Case a = cases.get(index);
        cases.add(index, cases.get(i));
        cases.add(i, a);
      }
    }
}
