package se.kth.pymdht;

import java.net.Inet4Address;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

class LookupQueue {

	private SortedSet<QueuedNode> queue;
	private SortedSet<QueuedNode> closest_responded_qnodes;
	private Set<Inet4Address> queued_ips;
	private Set<Inet4Address> queried_ips;
	private Id info_hash;
	
	public LookupQueue(Id info_hash){
		this.info_hash = info_hash;
		this.queue = new TreeSet<QueuedNode>();
		this.queued_ips = new HashSet<Inet4Address>();
		this.queried_ips = new HashSet<Inet4Address>();
		this.closest_responded_qnodes = new TreeSet<QueuedNode>();
		
	}
	
	public List<Node> bootstrap(List<Node> nodes){
		for (Node node : nodes){
			this.queried_ips.add(node.ip);
		}
		return nodes;
	}
	
	public List<Node> on_response(Node src_node, List<Node> nodes, int max_nodes){
		QueuedNode qnode = new QueuedNode(src_node,
				src_node.id.distance(this.info_hash));
		if (this.closest_responded_qnodes.size() > 0){
			QueuedNode last = this.closest_responded_qnodes.last();
			if (qnode.distance.log < last.distance.log){
				this.closest_responded_qnodes.remove(last);
				this.closest_responded_qnodes.add(qnode);
			}
		}
		for (Node node : nodes){
			qnode = new QueuedNode(node, node.id.distance(this.info_hash));
			if (!queued_ips.contains(qnode.node.ip)){
				queued_ips.add(qnode.node.ip);
				queue.add(qnode);
			}
		}
		return _pop_nodes_to_query(max_nodes);
	}
		
	public List<Node> on_timeout(int max_nodes){
		return _pop_nodes_to_query(max_nodes);
	}
		
	private List<Node> _pop_nodes_to_query(int max_nodes){
		List<Node> nodes_to_query = new ArrayList<Node>();
		
		int mark = Id.ID_SIZE_BITS;
		if (this.closest_responded_qnodes.size() > 0){
			mark = this.closest_responded_qnodes.last().distance.log;
		}
		QueuedNode qnode;
		while (this.queue.size() > 0 && nodes_to_query.size() < max_nodes){
			qnode = this.queue.first();
			this.queue.remove(qnode);
			if (qnode.distance.log < mark){
				this.queried_ips.add(qnode.node.ip);
				nodes_to_query.add(qnode.node);
				queued_ips.remove(qnode.node.ip);
			}
		}
		return nodes_to_query;
	}
	
}