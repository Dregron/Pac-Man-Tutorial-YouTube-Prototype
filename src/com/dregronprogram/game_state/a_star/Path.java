package com.dregronprogram.game_state.a_star;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.dregronprogram.application.Renderer;
import com.dregronprogram.display.Display;
import com.dregronprogram.game_state.Direction;
import com.dregronprogram.utils.MathUtils;
import com.dregronprogram.utils.Vector2;

public class Path implements Renderer {
	
	private Map<Vector2, Node> nodes = new HashMap<Vector2, Node>();

	public Path(Map<Vector2, Node> nodes) {
		this.nodes = nodes;
	}
	
	@Override
	public void update(double delta) {
		
	}

	@Override
	public void draw(Graphics2D g) {
		
	}
	
	public Map<Vector2, Node> getNodes() {
		return nodes;
	}
		
	public List<Direction> findPath(int startXPos, int startYPos, int goalXPos, int goalYPos) {
		if  (!nodes.containsKey(new Vector2(goalXPos, goalYPos, 0, 0)) || !nodes.containsKey(new Vector2(startXPos, startYPos, 0, 0))) 
			return new LinkedList<Direction>();
		
		List<Direction> queuedDirections = new LinkedList<Direction>();
		List<Node> openNodes = new LinkedList<Node>();
		List<Node> closedNodes = new LinkedList<Node>();
		Node startNode, goalNode;
		startNode = nodes.get(new Vector2(startXPos, startYPos, 0, 0));
		goalNode = nodes.get(new Vector2(goalXPos, goalYPos, 0, 0));
		startNode.setG_score(0);
		startNode.setF_score(getDistanceBetween(startNode, goalNode));
		openNodes.add(startNode);
		
		for (Node node : nodes.values()) {
			node.clearColour();
			node.clear();
		}
		
		while(!openNodes.isEmpty()) {
			Node current = getNodeWithLowestFScore(openNodes);
			
			closedNodes.add(current);
			openNodes.remove(current);

			if (MathUtils.isEqual(current.getxPos(), goalXPos, 0) && MathUtils.isEqual(current.getyPos(), goalYPos, 0)) {
				queueDirection(queuedDirections, current);
				for (Node node : current.getPrevNodesNode()) {
					queueDirection(queuedDirections, node);
				}
				break;
			}
			
			current.setColour(Color.CYAN);
			for (Node neighbor : getAdjacentBlocks(current)) {
				
				if (closedNodes.contains(neighbor)) {
					continue;
				}
				neighbor.setColour(Color.CYAN);
				int tentative_g_score = current.getG_score() + getDistanceBetween(current,neighbor);
				if (!openNodes.contains(neighbor)) {
					openNodes.add(neighbor);
				} else if (tentative_g_score >= neighbor.getG_score()) {
					continue;
				}
				
				neighbor.setG_score(tentative_g_score);
				neighbor.setF_score(neighbor.getG_score() + getDistanceBetween(neighbor, goalNode));
				neighbor.setNeighborX(getDX(current, neighbor));
				neighbor.setNeighborY(getDY(current, neighbor));
				neighbor.getPrevNodesNode().add(current);
				neighbor.getPrevNodesNode().addAll(current.getPrevNodesNode());
			}
		}
		return queuedDirections;
	}
	
	private Node getNodeWithLowestFScore(List<Node> openNodes) {
		int f_score = Integer.MAX_VALUE;
		Node n = null;
		for (Node node : openNodes) {
			if (node.getF_score() < f_score) {
				f_score = node.getF_score();
				n = node;
			}
		}
		return n;
	}

	private void queueDirection(List< Direction> queuedDirections, Node node) {
		if (node.getNeighborX() == 1) {
			queuedDirections.add(Direction.RIGHT);
		} else if (node.getNeighborX() == -1) {
			queuedDirections.add(Direction.LEFT);
		} else if (node.getNeighborY() == -1) {
			queuedDirections.add(Direction.UP);
		} else if (node.getNeighborY() == 1) {
			queuedDirections.add(Direction.DOWN);
		}
	}
	
	private List<Node> getAdjacentBlocks(Node current) {
		List<Node> bs = new ArrayList<Node>();
		Vector2 vector = new Vector2(0, 0);
		vector.set(current.getxPos()+current.getWidth(), current.getyPos());
		addAdjacentBlock(current, bs, vector);
		vector.set(current.getxPos()-current.getWidth(), current.getyPos());
		addAdjacentBlock(current, bs, vector);
		vector.set(current.getxPos(), current.getyPos()+current.getHeight());
		addAdjacentBlock(current, bs, vector);
		vector.set(current.getxPos(), current.getyPos()-current.getHeight());
		addAdjacentBlock(current, bs, vector);
		return bs;
	}
	
	private void addAdjacentBlock(Node current, List<Node> bs, Vector2 vector) {
		if (nodes.containsKey(vector)) {
			bs.add(nodes.get(vector));
		}
//		else if (current.isAdjacentRightFloor()) {
//			vector.set(0, current.getyPos());
//			bs.add(nodes.get(vector));
//		} else if (current.isAdjacentLeftFloor()) {
//			vector.set((Display.WIDTH-current.getWidth()), current.getyPos());
//			bs.add(nodes.get(vector));
//		}
	}
	
	private int getDX(Node current, Node neighbor) {
		int targetX = neighbor.getxPos() / neighbor.getWidth();
		int currentX = current.getxPos() / current.getWidth();
		int targetXRightMirror = (Display.WIDTH / neighbor.getWidth())+targetX;
		int targetXLeftMirror = -(Display.WIDTH/neighbor.getWidth())+targetX;
			
		int normalTarget = targetX-currentX;
		int rightMirrorTarget = targetXRightMirror-currentX;
		int leftMirrorTarget = targetXLeftMirror-currentX;
		   
		return normalTarget;
//		if (Math.abs(normalTarget) <= Math.abs(rightMirrorTarget) && Math.abs(normalTarget) <= Math.abs(leftMirrorTarget)) {
//			return normalTarget;
//		} else if (Math.abs(leftMirrorTarget) <= Math.abs(rightMirrorTarget)) {
//			if (leftMirrorTarget == 1) {
//				throw new RuntimeException("left wrong direction");
//			}
//			return leftMirrorTarget;
//		} else {  
//			if (rightMirrorTarget == -1) {
//				throw new RuntimeException("rightwrong direction");
//			}
//			return rightMirrorTarget;
//		}
	}
	
	private int getDY(Node current, Node neighbor) {
		int targetY = neighbor.getyPos() /  neighbor.getHeight();
		int currentY = current.getyPos() / current.getHeight();
		int normalTarget = targetY-currentY;
		
		return normalTarget;
	}
	
	private int getDistanceBetween(Node current, Node neighbor) {
		return Math.abs(getDX(current, neighbor)) + Math.abs(getDY(current, neighbor));
	}
}
