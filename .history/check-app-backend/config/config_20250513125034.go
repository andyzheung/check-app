package config

import (
	"os"

	"gopkg.in/yaml.v2"
)

type Config struct {
	Server struct {
		Port int    `json:"port"`
		Mode string `json:"mode"`
	} `json:"server"`
	Database struct {
		Host     string `yaml:"host"`
		Port     int    `yaml:"port"`
		User     string `yaml:"user"`
		Password string `yaml:"password"`
		DBName   string `yaml:"dbname"`
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
	f, err := os.Open("../config/config.yaml")
	if err != nil {
		return err
	}
	defer f.Close()

	decoder := yaml.NewDecoder(f)
	return decoder.Decode(&GlobalConfig)
}
