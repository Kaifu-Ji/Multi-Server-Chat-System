package com.server;

public class ClientMessageProcessor extends Thread
{
	volatile boolean stop = false;

	@Override
	public void run()
	{
		while (!stop)
		{
			try
			{
				Message message = ClientMessageQuene.getInstance().takeMessage();
				// System.out.println(message.jsonOperator.job.toJSONString());
				switch (message.jsonOperator.getType())
				{
				case "list":
					list(message);
					break;
				case "quit":
					quit(message);
				default:
					break;
				}
			} catch (InterruptedException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	private void list(Message m)
	{
		String messageSend = JsonOperator.constructList(RoomManager.getInstance().roomList());
		new MessageSender("responese", messageSend, null, m.identity).run();
	}

	private void quit(Message m)
	{
		String messageSend = JsonOperator.roomChange(m.identity,
				ClientManager.getInstance().getClient(m.identity).Room, "");
		new MessageSender("responese", messageSend, null, m.identity).run();
	}
}
