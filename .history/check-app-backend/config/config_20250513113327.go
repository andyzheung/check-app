package config

import (
	"os"

	"gopkg.in/yaml.v3"
)

type Config struct {
	Server struct {
		Port int    `yaml:"port"`
		Mode string `yaml:"mode"`
	} `yaml:"server"`
	Database struct {
		Host     string `yaml:"host"`
		Port     int    `yaml:"port"`
		User     string `yaml:"user"`
		Password string `yaml:"password"`
		DBName   string `yaml:"dbname"`
	} `yaml:"database"`
	JWT struct {
		Secret string `yaml:"secret"`
		Expire int    `yaml:"expire"` // 过期时间（小时）
	} `yaml:"jwt"`
	AD struct {
		Server   string `yaml:"server"`
		Port     int    `yaml:"port"`
		BaseDN   string `yaml:"base_dn"`
		BindDN   string `yaml:"bind_dn"`
		Password string `yaml:"password"`
	} `yaml:"ad"`
}

var GlobalConfig Config

// Load 加载配置文件
func Load() error {
	f, err := os.Open("config/config.yaml")
	if err != nil {
		return err
	}
	defer f.Close()

	decoder := yaml.NewDecoder(f)
	return decoder.Decode(&GlobalConfig)
}
