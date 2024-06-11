/// -------------------------------------------------------------------------------------
/// this is the only file you need to edit
/// -------------------------------------------------------------------------------------
///

#include "memsim.h"
#include <cassert>
#include <iostream>

// I suggest you implement the simulator as a class, like the one below.
// If you decide not to use this class, feel free to remove it.
struct Simulator {
  Simulator(int64_t page_size)
  {
    // constructor
  }
  void allocate(int tag, int size)
  {
    // Pseudocode for allocation request:
    // - search through the list of partitions from start to end, and
    //   find the largest partition that fits requested size
    //     - in case of ties, pick the first partition found
    // - if no suitable partition found:
    //     - get minimum number of pages from OS, but consider the
    //       case when last partition is free
    //     - add the new memory at the end of partition list
    //     - the last partition will be the best partition
    // - split the best partition in two if necessary
    //     - mark the first partition occupied, and store the tag in it
    //     - mark the second partition free
  }
  void deallocate(int tag)
  {
    // Pseudocode for deallocation request:
    // - for every partition
    //     - if partition is occupied and has a matching tag:
    //         - mark the partition free
    //         - merge any adjacent free partitions
  }
  MemSimResult getStats()
  {
    // hard-coded results
    MemSimResult result;
    result.max_free_partition_size = 123;
    result.max_free_partition_address = 321;
    result.n_pages_requested = 111;
    return result;
  }
  void check_consistency()
  {
    // mem_sim() calls this after every request to make sure all data structures
    // are consistent. Since this will probablly slow down your code, I suggest
    // you comment out the call below before submitting your code for grading.
    check_consistency_internal();
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
MemSimResult mem_sim(int64_t page_size, const std::vector<Request> & requests)
{
  // if you decide to use the simulator class above, you likely do not need
  // to modify the code below at all
  Simulator sim(page_size);
  for (const auto & req : requests) {
    if (req.tag < 0) {
      sim.deallocate(-req.tag);
    } else {
      sim.allocate(req.tag, req.size);
    }
    sim.check_consistency();
  }
  return sim.getStats();
}