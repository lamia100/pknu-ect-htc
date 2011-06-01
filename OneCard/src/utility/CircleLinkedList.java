package utility;

import player.Player;

public class CircleLinkedList <T>{
	private Node<T> headNode;
	private int count=0;
	public void add(T element)
	{
		if(count==0)
		{
			headNode=new Node<T>(element);
		}else if(count ==1)	{
			new Node<T>(element, headNode, headNode);			
		}else{
			new Node<T>(element,headNode.getPrev(),headNode);
		}
		count++;
	}
	public Node<T> remove(Player player)
	{
		
		return null;
	}
	public Node<T> getHead()
	{
		return headNode;
	}
	public int size() {
		// TODO Auto-generated method stub
		return count;
	}
}