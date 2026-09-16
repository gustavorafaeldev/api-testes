#!/usr/bin/env python3
import json
from mininet.net import Mininet
from mininet.link import TCLink
from mininet.log import setLogLevel
from mininet.node import OVSBridge
from mininet.topo import Topo

setLogLevel('warning')

class LabTopo(Topo):
    def build(self, loss=0):
        s1 = self.addSwitch('s1')
        h1 = self.addHost('h1')
        h2 = self.addHost('h2')
        opts = dict(bw=20, delay='20ms', loss=loss)
        self.addLink(h1, s1, **opts)
        self.addLink(h2, s1, **opts)

def available_algorithms():
    return open('/proc/sys/net/ipv4/tcp_available_congestion_control').read().split()

def run_scenario(label, loss):
    topo = LabTopo(loss=loss)
    net = Mininet(topo=topo, controller=None, link=TCLink, switch=OVSBridge)
    net.start()
    h1, h2 = net.get('h1', 'h2')
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
