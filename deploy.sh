echo "Building app..."
./mvnw clean package

echo "Deploy files to server..."
scp -r D:/JewleryMS/target/jewelry.jar root@143.198.216.177:/var/www/jewelry_be/

ssh -i ~/Desktop/jewelryManagementSystem root@143.198.216.177 <<EOF
pid=\$(sudo lsof -t -i :8080)

if [ -z "\$pid" ]; then
    echo "Start server..."
else
    echo "Restart server..."
    sudo kill -9 "\$pid"
fi
cd /var/www/jewelry_be
java -jar jewelry.jar
EOF

echo "Done!"
