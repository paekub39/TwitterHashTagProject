import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

public class TwitterController {
	static boolean isChooseSearchType = false;
	static boolean isNeedRetweet = false;
	static boolean isSelectLanguage = false;
	static boolean isSearchTweetCheck = false;
	static boolean isSearchTweet = false;
	static String input = "";
	static int checkInput = 0;
	static int option = 1;
	static int roundNumber = 1;
	static String fileName = "Tweets.txt";
	static File f = new File(fileName);
	
	static Scanner sc = new Scanner(System.in);
	
	static TwitterKey tk1 = new TwitterKey("FgZ2RfP7iXAt5n7ZicOqVPi7n", 
			"6JL8DwmgKB9AGHJenBGrrer5HkcLtCTA3y9OjTaLuTjUYCGaQ8", 
			"546477718-P2Y8SAOJirOv1Tpop5RTgsBnWH3LluzWA31v3hZO", 
			"J4FRKGuI7tK76V287dXhy5SPe77vhPi7tYRBi9v0EVkDI");
	static ConfigurationBuilder cb = new ConfigurationBuilder();

	public static void run() throws IOException{
		cb.setOAuthConsumerKey(tk1.getCustomerKey())
			.setOAuthConsumerSecret(tk1.getCustomerSecret())
			.setOAuthAccessToken(tk1.getTokenKey())
			.setOAuthAccessTokenSecret(tk1.getTokenSecret());

		TwitterFactory tf = new TwitterFactory(cb.build());
		TweetsFinder tf1 = new TweetsFinder(tf);
		tf1.setFileName(fileName);
		while(true){
			f.delete();
			while(!isSearchTweetCheck) {
				System.out.println("What you need to search.");
				System.out.println("Enter 1: Search user timeline");
				System.out.println("Enter 2: Search tweet");
				checkInput = sc.nextInt();
				if(checkInput == 1) {
					isSearchTweet = false;
					isSearchTweetCheck = true;
				}else if(checkInput == 2) {
					isSearchTweet = true;
					isSearchTweetCheck = true;
				}else {
					System.out.println("ERROR: Please input the numver again.");
					isSearchTweetCheck = false;
				}
			}
			if(isSearchTweet) {
				System.out.println("Please enter hashtag that you want to search. (For example #SIT)");
				input = sc.next();
				input = input.toLowerCase();
				while(!isSelectLanguage) {
					System.out.println("Please Select language that you need to search from Twitter.");
					System.out.println("Enter 1: English");
					System.out.println("Enter 2: Thailand");
					System.out.println("Other means any language.");
					System.out.println("***Word counting function can use only English language.");
					checkInput = sc.nextInt();
					if(checkInput == 1){
						tf1.setLanguage("en");
					}else if(checkInput == 2) {
						tf1.setLanguage("th");
					}else {
						tf1.setLanguage("");
					}
				}
				/*
				while(!isChooseSearchType){
					System.out.println("Please choose one");
					System.out.println("Enter 1 if you need to search include retweet");
					System.out.println("Enter 2 if you need to search exclude retweet");
					checkRetweet = sc.nextInt();
					System.out.println("Finding...");
					if(checkInput == 1){
						isChooseSearchType = true;
						isNeedRetweet = true;
					}else if(checkInput == 2){
						isChooseSearchType = true;
						isNeedRetweet = false;
					}
					else{
						isChooseSearchType = false;
						System.out.println("ERROR: Please input only 1 or 2");
					}
				}
				
				tf1.findTweets(input, isNeedRetweet);
				*/
				tf1.findTweets(input, false);
				while(option!=3){
					System.out.println("Select an option.");
					System.out.println("Enter 1: Show how many tweets hashtag has.");
					System.out.println("Enter 2: Show top 10 word that people said about the hashtag.");
					System.out.println("Enter 3: Search new hashtag.");
					
					option = sc.nextInt();
					if(option == 1){
						System.out.println(tf1.getTweetsCounting());
					}else if(option == 2){
						WordFrequency wf = new WordFrequency();
						wf.readFile(fileName, input);
					}else if(option == 3){
						option = 0;
						break;
					}
					
				}
			}else {
				System.out.println("Please enter user ID");
				input = sc.next();
				tf1.findUserTimeline(input);
				
				
			}
			
		}
		
	}
}
