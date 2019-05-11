-- MySQL Script generated by MySQL Workbench
-- 2019年05月02日 星期四 21时22分34秒
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `mydb` DEFAULT CHARACTER SET utf8 ;
USE `mydb` ;

-- -----------------------------------------------------
-- Table `mydb`.`label`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`label` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `main` VARCHAR(5) NOT NULL,
  `sub` VARCHAR(10) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `multi_UNIQUE` (`main` ASC, `sub` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`country`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`country` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `short` CHAR(2) NOT NULL,
  `full_en` VARCHAR(30) NOT NULL,
  `full_cn` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `short_UNIQUE` (`short` ASC),
  UNIQUE INDEX `countrycol_UNIQUE` (`full_en` ASC),
  UNIQUE INDEX `full_cn_UNIQUE` (`full_cn` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`press`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`press` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(15) NOT NULL,
  `website` VARCHAR(25) NOT NULL,
  `country` INT NOT NULL,
  `address` VARCHAR(45) NOT NULL,
  `establish_time` DATE NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC),
  UNIQUE INDEX `website_UNIQUE` (`website` ASC),
  UNIQUE INDEX `address_UNIQUE` (`address` ASC),
  UNIQUE INDEX `establish_time_UNIQUE` (`establish_time` ASC),
  INDEX `country_fk_idx` (`country` ASC),
  CONSTRAINT `country_fk`
    FOREIGN KEY (`country`)
    REFERENCES `mydb`.`country` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`author`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`author` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `country` INT NOT NULL,
  `born` DATE NOT NULL,
  `died` DATE NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `multi_UNIQUE` (`name` ASC, `country` ASC, `born` ASC, `died` ASC),
  INDEX `country_fk_idx` (`country` ASC),
  CONSTRAINT `country_fk`
    FOREIGN KEY (`country`)
    REFERENCES `mydb`.`country` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`book`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`book` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `author_id` INT NOT NULL,
  `press_id` INT NOT NULL,
  `label_id` INT NOT NULL,
  `version` VARCHAR(10) NOT NULL,
  `ISBN` VARCHAR(13) NOT NULL,
  `price` FLOAT NOT NULL,
  `pages` INT NOT NULL,
  `other_things` TEXT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `multi_UNIQUE` (`name` ASC, `author_id` ASC, `press_id` ASC, `version` ASC),
  INDEX `label_fk_idx` (`label_id` ASC),
  INDEX `publish_fk_idx` (`press_id` ASC),
  INDEX `author_fk_idx` (`author_id` ASC),
  UNIQUE INDEX `ISBN_UNIQUE` (`ISBN` ASC),
  CONSTRAINT `label_fk`
    FOREIGN KEY (`label_id`)
    REFERENCES `mydb`.`label` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `press_fk`
    FOREIGN KEY (`press_id`)
    REFERENCES `mydb`.`press` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `author_fk`
    FOREIGN KEY (`author_id`)
    REFERENCES `mydb`.`author` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(25) NOT NULL,
  `password_encode` BINARY(32) NOT NULL COMMENT 'SHA-256 encoded password',
  `is_male` TINYINT NULL,
  `email` VARCHAR(45) NOT NULL,
  `reg_time` DATETIME NOT NULL,
  `authority` TINYINT(2) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`wish_list`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`wish_list` (
  `user_id` INT NOT NULL,
  `book_id` INT NOT NULL,
  `add_time` DATETIME NOT NULL,
  `add_price` FLOAT NOT NULL,
  PRIMARY KEY (`user_id`, `book_id`),
  INDEX `book_fk_idx` (`book_id` ASC),
  CONSTRAINT `user_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `mydb`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `book_fk`
    FOREIGN KEY (`book_id`)
    REFERENCES `mydb`.`book` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`transaction`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`transaction` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `book_id` INT NOT NULL,
  `deal_time` DATETIME NOT NULL,
  `deal_price` FLOAT NOT NULL,
  `paied` TINYINT NOT NULL DEFAULT 0,
  `paied_time` DATETIME NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `multi_UNIQUE` (`user_id` ASC, `book_id` ASC),
  INDEX `book_fk_idx` (`book_id` ASC),
  CONSTRAINT `user_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `mydb`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `book_fk`
    FOREIGN KEY (`book_id`)
    REFERENCES `mydb`.`book` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`review`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`review` (
  `user_id` INT NOT NULL,
  `book_id` INT NOT NULL,
  `edit_time` DATETIME NOT NULL,
  `content` VARCHAR(100) NOT NULL,
  `star` ENUM('1', '2', '3', '4', '5') NOT NULL,
  PRIMARY KEY (`user_id`, `book_id`),
  INDEX `book_fk_idx` (`book_id` ASC),
  UNIQUE INDEX `content_UNIQUE` (`content` ASC),
  CONSTRAINT `user_fk0`
    FOREIGN KEY (`user_id`)
    REFERENCES `mydb`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `book_fk0`
    FOREIGN KEY (`book_id`)
    REFERENCES `mydb`.`book` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`danmu`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`danmu` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `book_id` INT NOT NULL,
  `page_num` INT NOT NULL COMMENT 'constrain: less or equal to the book_id\'s book pages',
  `content` VARCHAR(20) NOT NULL,
  `time` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `multi_UNIQUE` (`user_id` ASC, `book_id` ASC, `page_num` ASC, `content` ASC),
  INDEX `book_fk_idx` (`book_id` ASC),
  CONSTRAINT `user_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `mydb`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `book_fk`
    FOREIGN KEY (`book_id`)
    REFERENCES `mydb`.`book` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`readlist`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`readlist` (
  `id` INT NOT NULL,
  `create_user` INT NOT NULL,
  `title` VARCHAR(10) NOT NULL,
  `create_time` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `user_fk_idx` (`create_user` ASC),
  UNIQUE INDEX `multi_UNIQUE` (`create_user` ASC, `create_time` ASC),
  CONSTRAINT `user_fk`
    FOREIGN KEY (`create_user`)
    REFERENCES `mydb`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`readlist_follow`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`readlist_follow` (
  `user_id` INT NOT NULL,
  `readlist_id` INT NOT NULL,
  `follow_time` DATETIME NOT NULL,
  PRIMARY KEY (`user_id`, `readlist_id`),
  INDEX `menu_fk_idx` (`readlist_id` ASC),
  UNIQUE INDEX `multi_UNIQUE` (`user_id` ASC, `follow_time` ASC),
  CONSTRAINT `user_fk`
    FOREIGN KEY (`user_id`)
    REFERENCES `mydb`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `readlist_fk`
    FOREIGN KEY (`readlist_id`)
    REFERENCES `mydb`.`readlist` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`readlist_books`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`readlist_books` (
  `readlist_id` INT NOT NULL,
  `book_id` INT NOT NULL,
  `add_time` DATETIME NOT NULL,
  PRIMARY KEY (`readlist_id`, `book_id`),
  INDEX `book_fk_idx` (`book_id` ASC),
  CONSTRAINT `readlist_fk`
    FOREIGN KEY (`readlist_id`)
    REFERENCES `mydb`.`readlist` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `book_fk`
    FOREIGN KEY (`book_id`)
    REFERENCES `mydb`.`book` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`billboard_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`billboard_type` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name_regex` VARCHAR(30) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_regex_UNIQUE` (`name_regex` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`billboard`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`billboard` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `type` INT NOT NULL,
  `time` DATETIME NOT NULL,
  `description` VARCHAR(25) NOT NULL,
  `create_admin` INT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `multi_UNIQUE` (`type` ASC, `description` ASC),
  INDEX `admin_fk_idx` (`create_admin` ASC),
  CONSTRAINT `admin_fk`
    FOREIGN KEY (`create_admin`)
    REFERENCES `mydb`.`user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `type_fk`
    FOREIGN KEY (`type`)
    REFERENCES `mydb`.`billboard_type` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`book_author`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`book_author` (
  `book_id` INT NOT NULL,
  `author_id` INT NOT NULL,
  `is_translator` TINYINT NOT NULL,
  PRIMARY KEY (`book_id`, `author_id`),
  INDEX `author_fk_idx` (`author_id` ASC),
  CONSTRAINT `book_fk`
    FOREIGN KEY (`book_id`)
    REFERENCES `mydb`.`book` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `author_fk`
    FOREIGN KEY (`author_id`)
    REFERENCES `mydb`.`author` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `mydb`.`billboard_book`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `mydb`.`billboard_book` (
  `billboard_id` INT NOT NULL,
  `book_id` INT NOT NULL,
  PRIMARY KEY (`billboard_id`, `book_id`),
  INDEX `book_id_idx` (`book_id` ASC),
  CONSTRAINT `billboard_fk`
    FOREIGN KEY (`billboard_id`)
    REFERENCES `mydb`.`billboard` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `book_id`
    FOREIGN KEY (`book_id`)
    REFERENCES `mydb`.`book` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
