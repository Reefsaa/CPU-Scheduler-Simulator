# CPU Scheduler Simulator

A Java-based CPU Scheduler Simulator developed to demonstrate operating system concepts including process scheduling, memory management, multithreading, and process execution.

## Features

* **Shortest Job First (SJF)** scheduling
* **Round Robin** scheduling with a 5ms time quantum
* **Priority Scheduling (Non-Preemptive)**
* Process starvation detection
* Priority aging
* Dynamic memory allocation and deallocation
* Multithreaded job loading
* Gantt Chart / execution order
* Process performance statistics
* Average Waiting Time
* Average Turnaround Time

## Scheduling Algorithms

### 1. Shortest Job First (SJF)

A non-preemptive scheduling algorithm that selects the process with the shortest CPU burst time. If two processes have the same burst time, their arrival order is used as a tie-breaker.

### 2. Round Robin

A preemptive scheduling algorithm using a **5ms time quantum**. Processes that do not finish within their quantum are returned to the ready queue.

### 3. Priority Scheduling

A non-preemptive priority-based scheduling algorithm where a smaller priority number represents a higher priority. The simulator also implements starvation detection and priority aging.

## Multithreading

The simulator uses multiple threads to manage process loading:

* **FileReaderThread** reads jobs from `job.txt` and places them into the job queue.
* **JobLoaderThread** loads jobs into memory and moves them to the ready queue when sufficient memory is available.
* **MainThread** initializes the system and starts the scheduling process.

## Memory Management

The simulator models a system with **2048 MB of total memory**. Memory is allocated when a process is loaded and deallocated when the process terminates.

## Process Control Block

Each process is represented using a PCB containing information such as:

* Process ID
* Burst Time
* Priority
* Memory Required
* Process State
* Start Time
* Completion Time
* Waiting Time
* Turnaround Time
* Starvation Status

## Input

Processes are loaded from `job.txt`.

Example format:

```text
1:30:10;400
2:5:1;300
3:18:15;500
4:7:2;200
5:25:20;400
```

Each process contains:

```text
Process ID : Burst Time : Priority ; Memory Required
```

## Output

The simulator displays:

* Gantt Chart / execution order
* Process execution times
* Process termination times
* Waiting Time
* Turnaround Time
* Starvation status
* Average Waiting Time
* Average Turnaround Time

## Technologies

* Java
* Object-Oriented Programming
* Multithreading
* CPU Scheduling Algorithms
* Memory Management
* Process Management

## How to Run

1. Clone the repository.
2. Open the project in a Java IDE.
3. Make sure `job.txt` is in the project directory.
4. Update the file path in `FileReaderThread.java` if necessary.
5. Run `MainThread.java`.
6. Select a scheduling algorithm:

   * `1` — SJF
   * `2` — Round Robin
   * `3` — Priority Scheduling

## Project Purpose

This project demonstrates fundamental Operating Systems concepts by simulating CPU scheduling, process management, multithreading, and memory allocation in a Java environment.
