#!/usr/bin/env bash


server=http://localhost:8090

until $(curl -s --output /dev/null $server); do
  echo 'waiting for api...'
  sleep 3
done


curl -s "$server/wallet/1"; echo " Current balance"
curl -s -XPOST "$server/wallet/1/deposit/20"; echo " 20€ Deposit"
curl -s -XPOST "$server/wallet/1/withdraw/30"; echo " 30€ Withdrawal"
curl -s -XPOST "$server/wallet/1/topup/40"; echo " 40€ Topup"
curl -s "$server/wallet/1"; echo " Current balance"
