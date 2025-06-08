import os
from dotenv import load_dotenv

load_dotenv()

class Config:
	SECRET_KEY = os.environ.get('SECRET_KEY')
	DB_USER = os.environ.get('DB_USER')
	DB_PASSWORD = os.environ.get('DB_PASSWORD')
	DB_HOST = os.environ.get('DB_HOST')
	DB_PORT = int(os.environ.get('DB_PORT'))
	DB_NAME = os.environ.get('DB_NAME')

class DevelopmentConfig(Config):
	DEBUG = True
	"""Como hereda de Config, se pueden sobreescribir aqui las variables anteriores para pruebas"""

class ProductionConfig(Config):
	DEBUG = False

config_by_name = dict(
	development=DevelopmentConfig,
	production=ProductionConfig,
	default=DevelopmentConfig # Configuración por defecto si no se especifica otra
)
