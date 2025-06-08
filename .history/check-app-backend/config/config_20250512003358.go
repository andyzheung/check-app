package config

import (
	"encoding/json"
	"os"
)

type Config struct {
	Server struct {
		Port int    `json:"port"`
		Mode string `json:"mode"`
	} `json:"server"`
	Database struct {
		Host     string `json:"host"`
		Port     int    `json:"port"`
		User     string `json:"user"`
		Password string `json:"password"`
		DBName   string `json:"dbname"`
	} `json:"database"`
	JWT struct {
		Secret string `json:"secret"`
		Expire int    `json:"expire"` // 过期时间（小时）
	} `json:"jwt"`
	AD struct {
		Server   string `json:"server"`
		Port     int    `json:"port"`
		BaseDN   string `json:"base_dn"`
		BindDN   string `json:"bind_dn"`
		Password string `json:"password"`
	} `json:"ad"`
}

var GlobalConfig Config

func LoadConfig(path string) error {
	file, err := os.Open(path)
	if err != nil {
		return err
	}
	defer file.Close()

	decoder := json.NewDecoder(file)
	return decoder.Decode(&GlobalConfig)
}
