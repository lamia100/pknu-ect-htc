package rbtree;

import static rbtree.Node.Null;

import java.util.ArrayList;
import java.util.Stack;

public class RedBlackTree<T extends Comparable<T>> {
	@SuppressWarnings("unchecked")
	Node<T> root = Null;
	
	public RedBlackTree() {
		// TODO Auto-generated constructor stub
		
	}
	
	public T add(T element) {
		Node<T> node = new Node<T>(element);
		if (root != Null) {
			Node<T> cur = root;
			while (true) {
				int cmp = cur.compareTo(node);
				if (cmp < 0) {
					if (cur.getRight() != Null) {
						cur = cur.getRight();
					} else {
						cur.setRight(node);
						node.setParent(cur);
						break;
					}
				} else if (cmp > 0) {
					if (cur.getLeft() != Null) {
						cur = cur.getLeft();
					} else {
						cur.setLeft(node);
						node.setParent(cur);
						break;
					}
				} else {
					return cur.getElement();
				}
			}
			addFixup(node);
		} else {
			root = node;
			root.setBlack();
		}
		return node.getElement();
	}
	
	public void addFixup(Node<T> node) {
		Node<T> z = node;
		while (z.getParent().isRed()) {
			if (z.getParent() == z.getParent().getParent().getLeft()) {
				Node<T> y = z.getParent().getParent().getRight();
				if (y.isRed()) {
					z.getParent().setBlack();
					y.setBlack();
					z.getParent().getParent().setRed();
					z = z.getParent().getParent();
				} else {
					if (z == z.getParent().getRight()) {
						
						z = z.getParent();
						if (z == root) {
							root = z.getRight();
						}
						z.leftRotate();
					}
					z.getParent().setBlack();
					z.getParent().getParent().setRed();
					if (z.getParent().getParent() == root) {
						root = z.getParent().getParent().getLeft();
					}
					z.getParent().getParent().rightRotate();
				}
			} else {
				Node<T> y = z.getParent().getParent().getLeft();
				if (y.isRed()) {
					z.getParent().setBlack();
					y.setBlack();
					z.getParent().getParent().setRed();
					z = z.getParent().getParent();
				} else {
					if (z == z.getParent().getLeft()) {
						z = z.getParent();
						if (z == root) {
							root = z.getLeft();
						}
						z.rightRotate();
					}
					z.getParent().setBlack();
					z.getParent().getParent().setRed();
					if (z.getParent().getParent() == root) {
						root = z.getParent().getParent().getRight();
					}
					z.getParent().getParent().leftRotate();
				}
			}
		}
		root.setBlack();
	}
	
	public Node<T> search(T element) {
		Node<T> node = new Node<T>(element);
		Node<T> cur = root;
		while (cur != Null) {
			int cmp = cur.compareTo(node);
			if (cmp < 0) {
				cur = cur.getRight();
			} else if (cmp > 0) {
				cur = cur.getLeft();
			} else {
				break;
			}
		}
		return cur;
	}
	
	public void remove(T element) {
		Node<T> node = search(element);
		if (node != Null)
			remove(node);
	}
	
	@SuppressWarnings("unchecked")
	public void remove(Node<T> node) {
		//System.out.println(node.getElement() + " rm start");
		boolean isLeft = node.getParent().getLeft() == node;
		Node<T> cur = null;
		if (node == root) {
			if (node.getRight() == Null && node.getLeft() == Null) {
				root = Null;
			} else if (node.getRight() == Null) {
				root = root.getLeft();
				root.setParent(Null);
			} else if (node.getLeft() == Null) {
				root = root.getRight();
				root.setParent(Null);
			} else {
				cur = node.getRight();
				while (cur.getLeft() != Null) {
					cur = cur.getLeft();
				}
				node.setElement(cur.getElement());
				remove(cur);
			}
			return;
		}
		if (node.getRight() == Null && node.getLeft() == Null) {
			cur = node.getParent();
			if (isLeft) {
				cur.setLeft(Null);
			} else {
				cur.setRight(Null);
			}
		} else if (node.getRight() == Null) {
			cur = node.getLeft();
			if (isLeft) {
				node.getParent().setLeft(cur);
				cur.setParent(node.getParent());
			} else {
				node.getParent().setRight(cur);
				cur.setParent(node.getParent());
			}
		} else if (node.getLeft() == Null) {
			cur = node.getRight();
			if (isLeft) {
				node.getParent().setLeft(cur);
				cur.setParent(node.getParent());
			} else {
				node.getParent().setRight(cur);
				cur.setParent(node.getParent());
			}
		} else {
			// ¼®¼¼¼­
			cur = node.getRight();
			while (cur.getLeft() != Null) {
				cur = cur.getLeft();
			}
			//System.out.println(node.getElement() + " rm find suc");
			T temp = node.getElement();
			node.setElement(cur.getElement());
			cur.setElement(temp);
			remove(cur);
			return;
		}
		
		node.setParent(Null);
		node.setLeft(Null);
		node.setRight(Null);
		
		//System.out.println(node.getElement() + " rm fix");
		if (node.isBlack())
			removeFixup(cur);
		//System.out.println(node.getElement() + " rm end");
		
	}
	
	public void removeFixup(Node<T> node) {
		Node<T> x = node;
		while (x != root && x.isBlack()) {
			if (x == x.getParent().getLeft()) {
				//System.out.println("Case Left");
				Node<T> w = x.getParent().getRight();
				if (w.isRed()) {
					//System.out.println("Case 1");
					w.setBlack();
					x.getParent().setRed();
					if (x.getParent() == root) {
						root = x.getParent().getRight();
					}
					x.getParent().leftRotate();
					w = x.getParent().getRight();
				}
				if (w.getLeft().isBlack() && w.getRight().isBlack()) {
					//System.out.println("Case 2");
					w.setRed();
					x = x.getParent();
				} else if (w.getRight().isBlack()) {
					
					//System.out.println("Case 3");
					w.getLeft().setBlack();
					w.setRed();
					if (w == root) {
						root = w.getLeft();
					}
					w.rightRotate();
					w = x.getParent().getRight();
				} else {//if (w.isBlack() && w.getRight().isRed()) {
					//System.out.println("Case 4");
					if (x.getParent().isBlack())
						w.setBlack();
					else
						w.setRed();
					x.getParent().setBlack();
					w.getRight().setBlack();
					break;
				}
			} else {
				//System.out.println("Right");
				Node<T> w = x.getParent().getLeft();
				if (w.isRed()) {
					
					//System.out.println("Case 5");
					w.setBlack();
					x.getParent().setRed();
					if (x.getParent() == root) {
						root = x.getParent().getLeft();
					}
					x.getParent().rightRotate();
					w = x.getParent().getLeft();
				}
				if (w.getLeft().isBlack() && w.getRight().isBlack()) {
					
					//System.out.println("Case 6");
					w.setRed();
					x = x.getParent();
				} else if (w.getLeft().isBlack()) {
					
					//System.out.println("Case 7");
					w.getRight().setBlack();
					w.setRed();
					if (w == root) {
						root = w.getRight();
					}
					w.leftRotate();
					w = x.getParent().getLeft();
				} else {//if (w.isBlack() && w.getRight().isRed()) {
				
					//System.out.println("Case 8");
					if (x.getParent().isBlack())
						w.setBlack();
					else
						w.setRed();
					x.getParent().setBlack();
					w.getLeft().setBlack();
					break;
				}
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<T> getElementList() {
		Stack<Node<T>> stack = new Stack<Node<T>>();
		ArrayList<T> list = new ArrayList<T>();
		stack.push(Null);
		Node<T> n = root;
		while (!stack.isEmpty()) {
			if (n == Null) {
				n = stack.pop();
				list.add(n.getElement());
				n = n.getRight();
			} else {
				stack.push(n);
				n = n.getLeft();
			}
		}
		list.remove(list.size() - 1);
		return list;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<Node<T>> getNodeList() {
		Stack<Node<T>> stack = new Stack<Node<T>>();
		ArrayList<Node<T>> list = new ArrayList<Node<T>>();
		stack.push(Null);
		Node<T> n = root;
		while (!stack.isEmpty()) {
			if (n == Null) {
				n = stack.pop();
				list.add(n);
				n = n.getRight();
			} else {
				stack.push(n);
				n = n.getLeft();
			}
		}
		list.remove(list.size() - 1);
		return list;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		if (root == Null)
			return "root is Null";
		return "root=" + root.getElement() + "\n" + root.toString();
		
	}
	
	public int getMaxdepth() {
		return root.getMaxDepth();
	}
	
	public int getBlackHight() {
		Node<T> cur = root;
		int bh = 1;
		while (cur != Null) {
			if (cur.isBlack())
				bh++;
			cur = cur.getLeft();
		}
		return bh;
	}
/*
	public static void main(String[] args) {
		RedBlackTree<Integer> rb = new RedBlackTree<Integer>();
		java.util.Random rand = new java.util.Random();
		for (int i = 0; i < 100; i++) {
			rb.add(rand.nextInt());
		}
		ArrayList<Integer> list = rb.getElementList();
		//System.out.println("asdasd");
		int c = 0;
		for (Integer i : list) {
			//System.out.println(i);
			c++;
		}
		//System.out.println("c="+c);
		
	}
*/
}
