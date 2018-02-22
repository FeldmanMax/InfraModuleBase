#!/usr/bin/env bash
cont=$1 && docker stop $cont && docker rm $cont
