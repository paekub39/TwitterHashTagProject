
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
	
    static int numberOfKey = 4;
    static String twitterKey[][] = new String[numberOfKey][4];
    
    static int keyCount = 0;
    static int tweetsCountForChangeKey = 0;
    
	static final String hashtag = "#BlackPanther";
	static final int count = 65536;
	static long sinceId = 0;
	static long numberOfTweets = 0;
	static String collectTweets = "";
	
	
	
	
    static ConfigurationBuilder cb = new ConfigurationBuilder();

	public static void main(String[] args) throws IOException{
		
	    twitterKey[0][0] = "";
	    twitterKey[0][1] = "";
	    twitterKey[0][2] = "";
	    twitterKey[0][3] = "";
		
	    twitterKey[1][0] = "";
	    twitterKey[1][1] = "";
	    twitterKey[1][2] = "";
	    twitterKey[1][3] = "";
	    
	    twitterKey[2][0] = "";
	    twitterKey[2][1] = "";
	    twitterKey[2][2] = "";
	    twitterKey[2][3] = "";
	    
	    twitterKey[3][0] = "";
	    twitterKey[3][1] = "";
	    twitterKey[3][2] = "";
	    twitterKey[3][3] = "";
	    
	    
        fw=new FileWriter("Tweets.txt");
        bw=new BufferedWriter(fw);
		cb.setOAuthConsumerKey(twitterKey[0][0])
			.setOAuthConsumerSecret(twitterKey[0][1])
			.setOAuthAccessToken(twitterKey[0][2])
			.setOAuthAccessTokenSecret(twitterKey[0][3]);
		
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
	
	private static void getTweets(Query query, Twitter twitter, String mode) throws IOException{
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
					int forCount = 0;
					for(Status status: result.getTweets()){
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
						collectTweets=status.getText() + " Count " + (numberOfTweets+1) + " ";
						bw.write(collectTweets);
						forCount++;
					}
					numberOfTweets = numberOfTweets + result.getTweets().size();
					query.setMaxId(maxId-1);
					//tweetsCountForChangeKey = tweetsCountForChangeKey + result.getTweets().size();
					if(numberOfTweets >= 15000){
						tweetsCountForChangeKey = 0;
						break;
						/*
						if(keyCount >= 4) keyCount = 0;
						cb.setOAuthConsumerKey(twitterKey[keyCount][0])
							.setOAuthConsumerSecret(twitterKey[keyCount][1])
							.setOAuthAccessToken(twitterKey[keyCount][2])
							.setOAuthAccessTokenSecret(twitterKey[keyCount][3]);
						keyCount++;
						*/
					}
					
					System.out.println("Total tweets count ======= "+ numberOfTweets);
				}
			}catch (TwitterException te) {
                System.out.println("Couldn't connect: " + te);
                System.exit(-1);
            }catch (Exception e) {
                System.out.println("Something went wrong: " + e);
                System.exit(-1);
            }
			if(numberOfTweets >= 15000){
				tweetsCountForChangeKey = 0;
				break;
				/*
				if(keyCount >= 4) keyCount = 0;
				cb.setOAuthConsumerKey(twitterKey[keyCount][0])
					.setOAuthConsumerSecret(twitterKey[keyCount][1])
					.setOAuthAccessToken(twitterKey[keyCount][2])
					.setOAuthAccessTokenSecret(twitterKey[keyCount][3]);
				keyCount++;
				*/
			}

			whileCount++;
		}
		
		System.out.println("Total tweets count ======= "+ numberOfTweets);
		WordFrequency wf = new WordFrequency();
		wf.readFile();
	}
}