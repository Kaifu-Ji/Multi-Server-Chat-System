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
					break;
				case "who":
					who(message);
					break;
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
	
	private void who(Message m)
	{
		ClientInfo clientInfo = ClientManager.getInstance().getClient(m.identity);
		RoomInfo room = RoomManager.getInstance().getRoom(clientInfo.room);
		String messageSend = JsonOperator.who(room.listClients(), room.roomName, room.roomOwner);
		new MessageSender("response", messageSend, null, m.identity).run();
	}

	private void list(Message m)
	{
		String messageSend = JsonOperator.constructList(RoomManager.getInstance().roomList());
		new MessageSender("response", messageSend, null, m.identity).run();
	}

	private void quit(Message m)
	{
		String messageSend = JsonOperator.roomChange(m.identity,
				ClientManager.getInstance().getClient(m.identity).room, "");
		new MessageSender("response", messageSend, null, m.identity).run();
	}
}
