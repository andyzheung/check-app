package config

import (
	"os"

	"gopkg.in/yaml.v2"
)

type Config struct {
	Server struct {
		Port string `yaml:"port"`
		Mode string `yaml:"mode"`
	} `yaml:"server"`
	Database struct {
		Host      string `yaml:"host"`
		Port      int    `yaml:"port"`
		Username  string `yaml:"username"`
		Password  string `yaml:"password"`
		DBName    string `yaml:"dbname"`
		Charset   string `yaml:"charset"`
		ParseTime bool   `yaml:"parseTime"`
		Loc       string `yaml:"loc"`
	} `yaml:"database"`
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

// Load 加载配置文件
func Load() error {
	file, err := os.ReadFile("config/config.yaml")
	if err != nil {
		return err
	}

	err = yaml.Unmarshal(file, &GlobalConfig)
	if err != nil {
		return err
	}

	return nil
}
