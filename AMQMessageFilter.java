
import java.io.Serializable;
import java.util.Map;

import org.apache.activemq.artemis.api.core.ActiveMQException;
import org.apache.activemq.artemis.api.core.Message;
import org.apache.activemq.artemis.core.server.ServerSession;
import org.apache.activemq.artemis.core.server.plugin.ActiveMQServerPlugin;
import org.apache.activemq.artemis.core.transaction.Transaction;
import org.jboss.logging.Logger;

public class AMQMessageFilter implements ActiveMQServerPlugin, Serializable {

  private static final long serialVersionUID = 1L;
  private static final Logger logger = Logger.getLogger(AMQMessageFilter.class);
  private int maxMessageSize = 0;
  private static final String MAX_MESSAGE_SIZE = "maxMessageSize";

 public void init(Map<String, String> properties) {
    maxMessageSize = Integer.parseInt(properties.getOrDefault(MAX_MESSAGE_SIZE, "0"));
    logger.info("AMQMessageFilter plugin ::  maxMessageSize: "+ maxMessageSize);
  }

  public void beforeSend(ServerSession session, Transaction tx, Message message, boolean direct, boolean noAutoCreateQueue) throws ActiveMQException {
   // To get the real size of the message content
   long messageSize = message.toCore().getBodyBufferSize();
   // TODO uncomment the following if you are okay with an approximate message size (it will be slightly larger than the sent message)
   // long persistentSize = message.getPersistentSize();
    if (maxMessageSize > 0 && messageSize > maxMessageSize ) {
      throw new ActiveMQException("Rejecting message as the size "+messageSize+ " is greater than the configured max message size "+ maxMessageSize);
    }
  }

  public int getMaxMessageSize() {
    return maxMessageSize;
  }

  public void setMaxMessageSize(int maxMessageSize) {
    this.maxMessageSize = maxMessageSize;
  }
}
