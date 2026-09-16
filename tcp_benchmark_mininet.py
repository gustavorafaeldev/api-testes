#!/usr/bin/env python3
import json
import subprocess
from mininet.net import Mininet
from mininet.link import TCLink
from mininet.log import setLogLevel

setLogLevel('warning')

def available_algorithms():
    return open('/proc/sys/net/ipv4/tcp_available_congestion_control').read().split()

def run_scenario(label, loss):
    net = Mininet(controller=None, link=TCLink, build=False)
    h1 = net.addHost('h1')
    h2 = net.addHost('h2')
    net.addLink(h1, h2, bw=20, delay='20ms', loss=loss)
    net.build()
    net.start()
    try:
        h1.cmd('iperf3 -s -D')
        algos = available_algorithms()
        for algo in ('reno', 'cubic', 'bbr'):
            if algo not in algos:
                print(f'RESULT|{label}|{algo}|N/A|N/A', flush=True)
                continue
            raw = h2.cmd(f'iperf3 -c {h1.IP()} -t 10 -i 1 -C {algo} -J')
            data = json.loads(raw)
            rx = data['end']['sum_received']['bits_per_second'] / 1_000_000
            retx = data['end']['sum_sent'].get('retransmits', 0)
            print(f'RESULT|{label}|{algo}|{rx:.3f}|{retx}', flush=True)
    finally:
        h1.cmd('pkill -f iperf3 || true')
        net.stop()

print('=== BEGIN RESULTS ===', flush=True)
run_scenario('c1', 0)
run_scenario('c2', 2)
print('=== END RESULTS ===', flush=True)
