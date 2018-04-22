
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class Twitterss {
	
	static BufferedWriter bw=null;
    static FileWriter fw=null;
	
	static final String hashtag = "#BlackPanther";
	static final int count = 65536;
	static long sinceId = 0;
	static long numberOfTweets = 0;
	static String collectTweets = "";
	public static void main(String[] args) throws TwitterException, FileNotFoundException, IOException {

		ConfigurationBuilder cb = new ConfigurationBuilder();
        fw=new FileWriter("C:/Users/Pae/Desktop/Test.txt");
        bw=new BufferedWriter(fw);
		cb.setOAuthConsumerKey("FgZ2RfP7iXAt5n7ZicOqVPi7n")
			.setOAuthConsumerSecret("6JL8DwmgKB9AGHJenBGrrer5HkcLtCTA3y9OjTaLuTjUYCGaQ8")
			.setOAuthAccessToken("546477718-P2Y8SAOJirOv1Tpop5RTgsBnWH3LluzWA31v3hZO")
			.setOAuthAccessTokenSecret("J4FRKGuI7tK76V287dXhy5SPe77vhPi7tYRBi9v0EVkDI");
		
		TwitterFactory tf = new TwitterFactory(cb.build());
		Twitter twitter = tf.getInstance();
		
		/*List <Status> status = twitter.getHomeTimeline();
		for(Status st : status){
			System.out.println(st.getUser().getName()+"-------------------"+st.getText());
		}*/
		
		Query queryMax = new Query(hashtag);
		queryMax.setCount(count);
		getTweets(queryMax, twitter, "maxId");
		queryMax = null;

		do{
			Query querySince = new Query(hashtag);
			querySince.setCount(count);
			querySince.setSinceId(sinceId);
			getTweets(querySince, twitter, "sinceId");
			querySince = null;
		}while(checkIfSinceTweetsAreAvailable(twitter));
		
		bw.close();
	}
	
	private static boolean checkIfSinceTweetsAreAvailable(Twitter twitter){
		Query query = new Query(hashtag);
		query.setCount(count);
		query.setSinceId(sinceId);
		
		try{
			QueryResult result = twitter.search(query);
			if(result.getTweets()==null || result.getTweets().isEmpty()){
				query = null;
				return false;
			}
		}catch (TwitterException te) {
            System.out.println("Couldn't connect: " + te);
            System.exit(-1);
        }catch (Exception e) {
            System.out.println("Something went wrong: " + e);
            System.exit(-1);
        }
		
        return true;
	}
	
	private static void getTweets(Query query, Twitter twitter, String mode){
		boolean getTweets = true;
		long maxId = 0;
		long whileCount = 0;
		
		while(getTweets){
			try{
				QueryResult result = twitter.search(query);
				if(result.getTweets() == null || result.getTweets().isEmpty()){
					getTweets = false;
				}else{
					//System.out.println("***************************");
					//System.out.println("Gethered " + result.getTweets().size() + " tweets");
					long xx = 0;
					int forCount = 0;
					for(Status status: result.getTweets()){
						xx++;
						if(whileCount == 0 && forCount == 0){
							sinceId = status.getId();
							System.out.println("SinceID = "+sinceId);
						}/*
						System.out.println("Id = " + status.getId());
						System.out.println("@" + status.getUser().getScreenName() 
								+ " : " + status.getUser().getName()
								+ "---" + status.getText());*/
						if(forCount == result.getTweets().size()-1){
							maxId = status.getId();
							//System.out.println("maxId = " + maxId);
						}
						System.out.println("");
						collectTweets="\nCount: :" +xx+ ": >>>>>>>...ID: " + status.getId() + "::::::::" + status.getUser().getName() +"::::::::::"+ status.getText() + "::::Next::::\n";
						bw.write(collectTweets);
						forCount++;
					}
					numberOfTweets = numberOfTweets + result.getTweets().size();
					query.setMaxId(maxId-1);
					System.out.println("Total tweets count ======= "+ numberOfTweets);
				}
			}catch (TwitterException te) {
                System.out.println("Couldn't connect: " + te);
                System.exit(-1);
            }catch (Exception e) {
                System.out.println("Something went wrong: " + e);
                System.exit(-1);
            }
			whileCount++;
		}
		System.out.println("Total tweets count ======= "+ numberOfTweets);
	}
}