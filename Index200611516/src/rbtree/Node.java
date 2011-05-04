package rbtree;
@SuppressWarnings({"rawtypes" , "unchecked"})
public class Node <T extends Comparable<T>> implements Comparable<Node<T>> {
	public static final Node Null = new NULL();
	private boolean isRed = true;
	private Node<T> parent = Null;
	private Node<T> left = Null;
	private Node<T> right = Null;
	private T element;

	public T getElement() {
		return element;
	}

	public void setElement(T element) {
		this.element = element;
	}

	public Node(T element) {
		// TODO Auto-generated constructor stub
		this.element = element;

	}
	
	public Node<T> rightRotate() {
		Node p = parent;
		Node t = left;
		if (t != Null) {
			//s1
			if (p.left == this) {
				p.setLeft(t);
			} else {
				p.setRight(t);
			}
			t.setParent(p);
			//s2
			setLeft(t.getRight());
			t.getRight().setParent(this);
			//s3
			setParent(t);
			t.setRight(this);
		} else {
			throw new NullPointerException("Node.Null");
		}
		return t;
	}

	public Node<T> leftRotate() {
		Node p = parent;
		Node t = right;
		if (t != Null) {
			//s1
			if (p.left == this) {
				p.setLeft(t);
			} else {
				p.setRight(t);
			}
			t.setParent(p);
			//s2
			setRight(t.getLeft());
			t.getLeft().setParent(this);
			//s3
			setParent(t);
			t.setLeft(this);
		} else {
			throw new NullPointerException("Node.Null");
		}
		return t;
	}
	public int getMaxDepth() {
		int l = left.getMaxDepth();
		int r = right.getMaxDepth();
		return 1 + (l > r ? l : r);
	}

	@Override
	public String toString() {
		String str = left.toString() + element+" : "+isRed + ", " + right.toString();
		return str;
	}

	public boolean isRed() {
		return isRed;
	}

	public boolean isBlack() {
		return !isRed;
	}

	public void setBlack() {
		isRed = false;
	}

	public void setRed() {
		isRed = true;
	}

	@Override
	public int compareTo(Node<T> o) {
		// TODO Auto-generated method stub
		return element.compareTo(o.element);
	}

	public Node<T> getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public Node<T> getLeft() {
		return left;
	}

	public void setLeft(Node left) {
		this.left = left;
	}

	public Node<T> getRight() {
		return right;
	}

	public void setRight(Node right) {
		this.right = right;
	}

	private static class NULL extends Node  {

		public NULL() {
			super(null);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void setBlack() {
			// TODO Auto-generated method stub
			//super.setBlack();
		}
		@Override
		public void setRed() {
			// TODO Auto-generated method stub
			//super.setRed();
		}

		@Override
		public boolean isBlack() {
			// TODO Auto-generated method stub
			return true;
		}

		@Override
		public boolean isRed() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public String toString() {
			// TODO Auto-generated method stub
			return "";
		}

		@Override
		public int getMaxDepth() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public void setLeft(Node left) {
			// TODO Auto-generated method stub
			//super.setLeft(left);
		}

		@Override
		public Node getLeft() {
			// TODO Auto-generated method stub
			return this;
		}
		@Override
		public Node getRight() {
			// TODO Auto-generated method stub
			return this;
		}
		@Override
		public Node getParent() {
			// TODO Auto-generated method stub
			return this;
		}
		@Override
		public void setParent(Node parent) {
			// TODO Auto-generated method stub
			//super.setParent(parent);
		}

		@Override
		public void setRight(Node right) {
			// TODO Auto-generated method stub
			super.setRight(right);
		}
		@Override
		public int compareTo(Node o) {
			// TODO Auto-generated method stub
			return -1;
		}

	}
}