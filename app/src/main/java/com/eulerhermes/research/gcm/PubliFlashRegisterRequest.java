package com.eulerhermes.research.gcm;

import com.eulerhermes.research.BuildConfig;
import com.eulerhermes.research.model.PubliFlashRegisterResult;
import com.eulerhermes.research.network.rest.BaseRequest;
import com.google.api.client.http.HttpContent;

public class PubliFlashRegisterRequest extends BaseRequest<PubliFlashRegisterResult>
{
	private static String URL = BuildConfig.SERVER_OLD + "/WebServices/ws.asmx/JoinNotification";
	
	private final String deviceToken;
	
	public PubliFlashRegisterRequest(String deviceToken)
	{
		super(PubliFlashRegisterResult.class);
		this.deviceToken = deviceToken;
	}

	@Override
	protected String getURL()
	{
		return URL;
	}
	
	@Override
	protected int getRequestType()
	{
		return POST;
	}

	@Override
	protected HttpContent getContent()
	{
		String udid = "";//Android.getDeviceUDID(EulerHermesApplication.getInstance());
//		String deviceToken = Android.getDeviceUDID(EulerHermesApplication.getInstance());
		return contentFromString("{\"appName\":\"eulerhermes\", \"udid\":\"" + udid + "\",\"deviceToken\":\"" + deviceToken + "\"}");
	}
}
