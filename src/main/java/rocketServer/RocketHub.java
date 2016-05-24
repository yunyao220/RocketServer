package rocketServer;

import java.io.IOException;

import exceptions.RateException;
import netgame.common.Hub;
import rocketBase.RateBLL;
import rocketData.LoanRequest;


public class RocketHub extends Hub {

	private RateBLL _RateBLL = new RateBLL();
	
	public RocketHub(int port) throws IOException {
		super(port);
	}

	@Override
	protected void messageReceived(int ClientID, Object message) {
		System.out.println("Message Received by Hub");
		
		if (message instanceof LoanRequest) {
			resetOutput();
			
			LoanRequest lq = (LoanRequest) message;
			double rate=0.0;
			double payment = 0.0;
			try {
				rate = _RateBLL.getRate(lq.getiCreditScore());
				payment = _RateBLL.getPayment(rate,lq.getiTerm(), lq.getdHouseCost(), lq.getdExpenses(), true);
				lq.setdPayment(payment);
				
			} catch (RateException e) {
				lq.setErrorMessage(e.getMessage());
			}
			sendToAll(lq);
		}
	}
}
