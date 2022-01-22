package com.appdeveloperblog.app.ws.shared;

import org.springframework.stereotype.Service;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.appdeveloperblog.app.ws.share.dto.UserDto;
 
@Service
public class AmazonSES {
	final String AWS_URL = "ec2-35-88-121-223.us-west-2.compute.amazonaws.com:8080";
	final String LOCAL_URL = "localhost:8000";
	final String FINAL_URL = AWS_URL;
	// This address must be verified with Amazon SES.
	final String FROM = "kongjiajun555@outlook.com";

	// The subject line for the email.
	final String SUBJECT = "Email verification to complete your registration";
	
	final String PASSWORD_RESET_SUBJECT = "Password reset request";

	// The HTML body for the email.
	final String HTMLBODY = "<h1>Please verify your email address</h1>"
			+ "<p>To complete registration process and be able to log in, "
			+ "click on the link: "
			+ "<a href='http://" + FINAL_URL + "/verification-service/email-verification.html?token=$tokenValue'>"
			+ "Final step to complete your registration" + "</a><br/><br/>"
			+ "Thank you!";

	// The email body for recipients with non-HTML email clients.
	final String TEXTBODY = "Please verify your email address. "
			+ "To complete registration process and be able to log in, "
			+ "open the link in your browser: "
			+ "http://" + FINAL_URL + "/verification-service/email-verification.html?token=$tokenValue"
			+ " Thank you!";
	
            //ec2-54-188-81-220.us-west-2.compute.amazonaws.com:8080
	
	final String PASSWORD_RESET_HTMLBODY = "<h1>Resetting your password</h1>"
		      + "<p>Hi, $firstName!</p> "
		      + "<p>You are attempting to reset your password. Ignore if you did not request. "
		      + "Otherwise please click on the link to create a new password: " 
		      + "<a href='http://" + FINAL_URL + "/verification-service/password-reset.html?token=$tokenValue'> "
		      + "Click on this link to Reset Password"
		      + "</a><br/><br/>"
		      + "Thank you!";

		  // The email body for recipients with non-HTML email clients.
		  final String PASSWORD_RESET_TEXTBODY = "Resetting your password "
		      + "Hi, $firstName! "
		      + "You are attempting to reset your password. Ignore if you did not request. "
		      + "Otherwise please open the link to create a new password: " 
		      + "\nhttp://" + FINAL_URL +"/verification-service/password-reset.html?token=$tokenValue "
		      + "Thank you!";
	

	public void verifyEmail(UserDto userDto) {

		// You can also set your keys this way. And it will work!
		//System.setProperty("aws.accessKeyId", "<YOUR KEY ID HERE>"); 
		//System.setProperty("aws.secretKey", "<SECRET KEY HERE>"); 
		
		AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard().withRegion(Regions.US_WEST_2)
				.build();
 
		String htmlBodyWithToken = HTMLBODY.replace("$tokenValue", userDto.getEmailVerificationToken());
		String textBodyWithToken = TEXTBODY.replace("$tokenValue", userDto.getEmailVerificationToken());

		SendEmailRequest request = new SendEmailRequest()
				.withDestination(new Destination().withToAddresses(userDto.getEmail()))
				.withMessage(new Message()
						.withBody(new Body().withHtml(new Content().withCharset("UTF-8").withData(htmlBodyWithToken))
								.withText(new Content().withCharset("UTF-8").withData(textBodyWithToken)))
						.withSubject(new Content().withCharset("UTF-8").withData(SUBJECT)))
				.withSource(FROM);

		client.sendEmail(request);
		System.out.println("Email sent!");
	}

	 public boolean sendPasswordResetRequest(String firstName, String email, String token)
	  {
	      boolean returnValue = false;
	 
	      AmazonSimpleEmailService client = 
	          AmazonSimpleEmailServiceClientBuilder.standard()
	            .withRegion(Regions.US_WEST_2).build();
	      
	      String htmlBodyWithToken = PASSWORD_RESET_HTMLBODY.replace("$tokenValue", token);
	             htmlBodyWithToken = htmlBodyWithToken.replace("$firstName", firstName);
	        
	      String textBodyWithToken = PASSWORD_RESET_TEXTBODY.replace("$tokenValue", token);
	             textBodyWithToken = textBodyWithToken.replace("$firstName", firstName);
	      
	      
	      SendEmailRequest request = new SendEmailRequest()
	          .withDestination(
	              new Destination().withToAddresses( email ) )
	          .withMessage(new Message()
	              .withBody(new Body()
	                  .withHtml(new Content()
	                      .withCharset("UTF-8").withData(htmlBodyWithToken))
	                  .withText(new Content()
	                      .withCharset("UTF-8").withData(textBodyWithToken)))
	              .withSubject(new Content()
	                  .withCharset("UTF-8").withData(PASSWORD_RESET_SUBJECT)))
	          .withSource(FROM);

	      SendEmailResult result = client.sendEmail(request); 
	      if(result != null && (result.getMessageId()!=null && !result.getMessageId().isEmpty()))
	      {
	          returnValue = true;
	      }
	      return returnValue;
	  }

}