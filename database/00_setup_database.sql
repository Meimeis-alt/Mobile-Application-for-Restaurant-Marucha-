-- =========================================================
-- Marucha Mobile App - Full Database Setup Script
-- =========================================================
-- This script recreates the entire database from scratch:
-- 1. Creates the database
-- 2. Creates all tables
-- 3. Adds constraints and indexes
-- 4. Inserts initial seed data
-- =========================================================

SOURCE schema/01_create_database.sql;
SOURCE schema/02_create_tables.sql;
SOURCE schema/03_constraints.sql;
SOURCE seed/01_roles.sql;
SOURCE seed/02_admin_user.sql;
SOURCE seed/03_sample_data.sql;