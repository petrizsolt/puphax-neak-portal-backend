package hu.neak.puphax.syncronizer.mapper;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

public class SoapReceiveEncodingUtf8Interceptor extends AbstractPhaseInterceptor<Message> {
	
    public SoapReceiveEncodingUtf8Interceptor() {
        super(Phase.RECEIVE);
    }

	@Override
	public void handleMessage(Message message) throws Fault {
		message.put(Message.ENCODING, "UTF-8");
	}
}
