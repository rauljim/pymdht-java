package se.kth.pymdht;

import java.net.InetSocketAddress;

public class Node {
	public InetSocketAddress addr;
	public Id id;
	public Node(InetSocketAddress addr, Id node_id){
		//  debug only 
		this.addr = addr;
		this.id = node_id;
//		this._compact_addr = utils.compact_addr(addr);
	}
	//  def get_id(self): 
	//      return self._id 
	//  def set_id(self, node_id): 
	//      if self._id is None: 
	//          self._id = node_id 
	//      else: 
	//          raise AttributeError, "Node's id is read-only" 
	//      return 
	//  def addr(self): 
	//      return self._addr 
	//  def compact_addr(self): 
	//      return self._compact_addr 
	//  def ip(self): 
	//      return self._addr[0] 
	//  def __eq__(self, other): 
	//      if self.addr == other.addr: 
	//          try: 
	//              return self.id == other.id 
	//          except AttributeError: 
	// self.id == None (id._bin fails) 
	//              return self.id is None and other.id is None 
	//      else: 
	//          return False 
	//  def __ne__(self, other): 
	//      return not self == other 
	//  def __hash__(self): 
	//      if self.id: 
	//          return self.addr.__hash__() ^ self.id.__hash__() 
	//      else: 
	//          return self.addr.__hash__() 
	//  def __repr__(self): 
	//      return '<node: %26r %r (%s)>' % (self.addr, 
	//                                     self.id, 
	//                                     self.version) 
	public Id distance(Node other){
		return this.id.distance(other.id);
	}
	public int log_distance(Node other){
		//  Only for backward compatibility. It will be removed. 
		return this.distance(other).log;
	}
//	public void compact(){
//		//Return compact format
//		return (this.id._bin+this.compact_addr);
//	}
}
