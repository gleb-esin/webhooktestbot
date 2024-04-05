#!/bin/bash

properties_file="C:/Users/Gleb/IdeaProjects/Durak/Telegram_bot_Durak/throwinfoolbot/src/main/resources/telegram.properties"

# Чтение значений переменных из файла telegram.properties
userName=$(grep '^userName' "$properties_file" | cut -d'=' -f2)
botToken=$(grep '^botToken' "$properties_file" | cut -d'=' -f2)
webHookPath=$(grep '^webHookPath' "$properties_file" | cut -d'=' -f2)

echo "Сборка проекта..."
mvn clean package

echo "Регистрация вебхука..."
curl -X POST "https://api.telegram.org/bot$botToken/setWebhook?url=$webHookPath"

echo "Копирование файла на удаленный сервер..."
scp "C:\Users\Gleb\IdeaProjects\Durak\Telegram bot Durak\throwinfoolbot\target\MeetSchedulerBot.jar" username@77.232.128.56:/home/username/Docker/msbtest

echo "Бэкап контейнера $userName..."
backup_filename="$userName_test_backup_$(date +'%Y-%m-%d_%H-%M-%S').tar"
ssh username@77.232.128.56 docker export $userName > $backup_filename

echo "Остановка контейнера $userName..."
ssh username@77.232.128.56 docker stop $userName

echo "Удаление контейнера $userName..."
ssh username@77.232.128.56 docker rm $userName

echo "Выполнение docker-compose.yml на удаленном сервере..."
ssh username@77.232.128.56 docker-compose -f /home/username/Docker/$userName/docker-compose.yml up -d