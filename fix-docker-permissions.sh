#!/bin/bash

# Add the current user to the Docker group
sudo usermod -aG docker $USER

# Change permissions for the Docker daemon socket
sudo chmod 666 /var/run/docker.sock

# Restart Docker service to apply changes
sudo systemctl restart docker

echo "Permissions fixed. Please log out and log back in for changes to take effect."
