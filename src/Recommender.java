import java.util.ArrayList;

public class Recommender {
	
	public void init()
	{
		
	}
	
	
	public double predictRating(Person p, Movie m)
	{
		ArrayList<Person> neighbor =  createNBH(p);
		double prediction = createPre(neighbor, m);
		return prediction;
	}

}
