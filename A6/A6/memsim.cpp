


/// -------------------------------------------------------------------------------------
/// this is the only file you need to edit
/// -------------------------------------------------------------------------------------
///
/// (c) 2023, Pavol Federl, pfederl@ucalgary.ca
/// Do not distribute this file.

#include "memsim.h"
#include <set>
#include <list>
#include <cassert>
#include <iostream>
#include <unordered_map>

//Code given in the hints in the document given by thr proff
//Citation starts
struct Partition {
	int tag;
	int64_t size, addr;
};

typedef std::list<Partition>::iterator PartitionRef;

struct scmp {
	bool operator()(const PartitionRef& c1, const PartitionRef& c2) const {
		if (c1->size == c2->size) {
			return c1->addr < c2->addr;
		}
		else {
			return c1->size > c2->size;
		}
	}
};

// I suggest you implement the simulator as a class, like the one below.
// If you decide not to use this class, feel free to remove it.
struct Simulator {
	std::list<Partition> all_blocks;
	std::unordered_map<long, std::vector<PartitionRef>> tagged_blocks;
	std::set<PartitionRef, scmp> free_blocks;
//Citation ends

	int64_t page_size;
	int64_t pages_requested;

	//It takes an integer value page_size as input
	Simulator(int64_t page_size) {
		this->pages_requested = 0;		//Initialize it to the zero
		this->page_size = page_size; 	//Initialize it to the value passed as a parameter.
	}

	// This function returns an iterator to the last partition block in the list of all partition blocks.
	PartitionRef get_last_pt() {
		auto& pts = this->all_blocks;
		auto pt_last = pts.end();
		if (pts.empty()) {				//If list is empty, it returns an iterator to the end of the list.
			return pt_last;
		}
		return std::prev(pt_last);		//It gets the iterator to the previous partition block from the end.
	}

	// This function returns the max size of the first partition block in the list of free partition blocks.
	int64_t get_max_free_pt_size() {
		auto& fb = this->free_blocks;
		if (fb.size() > 0) {
			return (*fb.begin())->size;
		}
		return 0ll;					//If there are no blocks, it returns 0.
	}

	//This function returns the address of the first partition block with the max size
	int64_t get_max_free_pt_addr() {
		auto& fb = this->free_blocks;
		if (fb.size() > 0) {
			return (*fb.begin())->addr;
		}
		return 0ll;					//If there are no free partition blocks, then it returns 0.
	}

	
	//This function allocates partition block and pushes it to the list 
	//It calculates the number of pages required based on the size requested and the page size.
	//Updates the no of pages requested 
	//Creates a partition block and adds to the list of partition block
	PartitionRef allocate_pt(int tag, int size) {
		auto& pts = this->all_blocks;
		auto pt_last = this->get_last_pt();
		if (pt_last != pts.end() && pt_last->tag == 0) {
			size -= pt_last->size;
		}
		auto pages = size / this->page_size;
		if (pages * this->page_size < size) {
			pages += 1;
		}
		this->pages_requested += pages;
		auto pt = Partition();
		pt.size = pages * this->page_size;
		if (pts.size() == 0) {
			pt.addr = 0;
		}
		else {
			pt.addr = pt_last->addr + pt_last->size;
		}
		pts.push_back(pt);
		if (pt_last == pts.end()) {
			pt_last = pts.begin();
		}
		else {
			pt_last++;
		}
		return pt_last;		//it returns a reference to the newly allocated partition block.
	}

	//This function breaks one partition to two separate. (one occupied, another is free).
	PartitionRef split_pt(PartitionRef pt, int tag, int size) {
		this->remove_free_pt(pt);								// Remove 'pt' from the list of free blocks.
		auto used_pt = Partition();
		pt->tag = 0;											// Set the tag of 'pt' to 0
		used_pt.size = size;
		pt->size = pt->size - size;								//updates size of pt for the size of used block
		used_pt.addr = pt->addr;
		pt->addr = pt->addr + size;								// Update the address of 'pt' to account for the used block.
		auto inserted = this->all_blocks.insert(pt, used_pt);
		this->insert_free_pt(pt);								// Add 'pt' back to the list of free blocks.
		return inserted;
	}

	//This function merges the free block immediately after the free block and returns resulting merged block.
	PartitionRef merge_next_pt(PartitionRef pt) {
		if (this->all_blocks.size() > 1 && 
			  pt != this->all_blocks.end()) {
			auto next_pt = std::next(pt);
			if (next_pt != this->all_blocks.end()) { 	// Check if the next block does exists and is free.
				if (next_pt->tag == 0) {
					pt->size += next_pt->size;			// If the next block is free, it merges
					this->remove_free_pt(next_pt);
					this->all_blocks.erase(next_pt);
				}
			}
		}
		return pt;
	}

	//The function below merges the free block with the previous and returns resulting merged block.
	PartitionRef merge_prev_pt(PartitionRef pt) {
		if (this->all_blocks.size() > 1 && 
			  pt != this->all_blocks.begin()) {
			auto prev_pt = std::prev(pt);
			if (prev_pt->tag == 0) {				//checks if the previous block is free
				remove_free_pt(prev_pt);
				prev_pt->size += pt->size;
				this->remove_free_pt(pt);			//Removes the pt and return merged block
				this->all_blocks.erase(pt);
				return prev_pt;
			}
		}
		return pt;									//if theres no free block, it returns back unmodified.
	}

	//this function merges the free block with the adjacent free blocks 
	PartitionRef merge_adjacent_pts(PartitionRef pt) {
		pt = this->merge_prev_pt(pt);				//Merges the previous block
		return this->merge_next_pt(pt);				//Merges the next free block and results merged block
	}

	// Define a function that inserts tagged partition to `tagged_blocks` hashtable.
	void insert_tagged_pt(int tag, PartitionRef pt) {
		auto& tb = this->tagged_blocks;
		if (tb.find(tag) == tb.end()) {				//checks if tag is in map of tagged blocks
			tb[tag] = std::vector<PartitionRef>();
		}
		tb[tag].push_back(pt);						//this appends to the given tag
	}

	
	//This inserts a free partition block in the set of free blocks.
	void insert_free_pt(PartitionRef pt) {
		this->remove_free_pt(pt);			//Removes it from the set of free blocks
		this->free_blocks.insert(pt);		//Insert it into the set of free blocks
	}

	//define a function that removes free block from the set of free blocks.
	void remove_free_pt(PartitionRef pt) {
		auto& fb = this->free_blocks;
		if (fb.find(pt) != fb.end()) {		//checks if it is in the set of free blocks
			fb.erase(pt);
		}
	}

	//Defining a function allocate which takes integer inout and size as input
	void allocate(int tag, int size) {
		auto pt_found = false;					//initializes bool pt_found to false
		auto& fb = this->free_blocks;
		PartitionRef pt_max;
		if (!fb.empty()) {						//This checks if the free_block listis not empty			
			pt_max = *fb.begin();				//If its not empty , sets pt_max to the beginning of the list
			if (pt_max->size >= size) {
				pt_found = true;
			}
		}
		if (!pt_found) {						//if it's false, it allocates new partition with the requested
			pt_max = this->allocate_pt(tag, size);
			pt_max = this->merge_prev_pt(pt_max);
		}
		if (pt_found && pt_max->size == size) {
			remove_free_pt(pt_max);
		}
		else {									//if it is false or size is greater than the requested size
			if (pt_max->size > size) {
				pt_max = this->split_pt(pt_max, tag, size);
			}
		}
		pt_max->tag = tag;						//sets the pt_max to the requested tag
		this->insert_tagged_pt(tag, pt_max);	//inserts pt_max to tagged_block
	}

	
	//Defining a function deallocate which takes integer input tag as input
	void deallocate(int tag) {
		auto& tb = this->tagged_blocks;
		if (tb.find(tag) == tb.end()) {		//Checks if the tag is not present in the block
			return;
		}
		auto& pts = tb[tag];
		for (auto ipt = pts.begin(); ipt != pts.end(); ipt++) {
			auto pt = *ipt;
			if (pt->tag != 0 && pt->tag == tag) {	//if part.pointer.tag != 0, it matches the tag and resets the tag to 0.
				pt->tag = 0;
				pt = this->merge_adjacent_pts(pt);
				this->insert_free_pt(pt);
			}
		}
		pts.clear();
	}

	//Defining a function getStats that returns MemSimResult
	MemSimResult getStats() {
		auto result = MemSimResult();
		result.n_pages_requested = this->pages_requested;
		result.max_free_partition_size = this->get_max_free_pt_size();
		result.max_free_partition_address = this->get_max_free_pt_addr();
		return result;
	}

	//Code cited from the starter code given by the proff.
	//Citation Starts
	void check_consistency() 
	{
		// mem_sim() calls this after every request to make sure all data structures
    	// are consistent. Since this will probablly slow down your code, I suggest
    	// you comment out the call below before submitting your code for grading.
		// check_consistency_internal();
	}
	void check_consistency_internal() 
	{
		// you do not need to implement this method at all - this is just my suggestion
    	// to help you with debugging

    	// here are some suggestions for consistency checks (see appendix also):

    	// make sure the sum of all partition sizes in your linked list is
    	// the same as number of page requests * page_size

    	// make sure your addresses are correct

    	// make sure the number of all partitions in your tag data structure +
    	// number of partitions in your free blocks is the same as the size
    	// of the linked list

    	// make sure that every free partition is in free blocks

    	// make sure that every partition in free_blocks is actually free

    	// make sure that none of the partition sizes or addresses are < 1
	}
};

// re-implement the following function
// ===================================
// parameters:
//    page_size: integer in range [1..1,000,000]
//    requests: array of requests
// return:
//    some statistics at the end of simulation

MemSimResult mem_sim(int64_t page_size, const std::vector<Request>& requests) 
{
	// if you decide to use the simulator class above, you likely do not need
  	// to modify the code below at all
	Simulator sim(page_size);
	for (const auto & req : requests) {
		if (req.tag < 0) {
			sim.deallocate(-req.tag);
		} 
		else {
			sim.allocate(req.tag, req.size);
		}
		sim.check_consistency();
	}
	return sim.getStats();
}

//Citation ends
