#! /usr/bin/env python

import time
import sys
import socket

from identifier import Id

handshake_head = ''.join(('\0'*4,
                          '\4',
                          '\0'*4,
                          ))
handshake_tail = ''.join(('\0',
                          '\1\2\3\4',
                          ))
info_hash = 'd9f7719922d00d4f2ae59c514dc2aee7a2938dc9'

info_hash = Id(info_hash).bin_id


handshake = ''.join((handshake_head, info_hash, handshake_tail))

s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
s.sendto(handshake, ('127.0.0.1', int(sys.argv[1])))
print 'sending...'
start_ts = time.time()

while 1:
    data, addr = s.recvfrom(2000)
    print time.time() - start_ts, len(data), 'bytes from', addr
