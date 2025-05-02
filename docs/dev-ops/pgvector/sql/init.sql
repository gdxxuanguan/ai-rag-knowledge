-- 启用 vector 扩展
CREATE EXTENSION IF NOT EXISTS vector;

-- 创建 vector_store 表
CREATE TABLE IF NOT EXISTS vector_store (
                                            id UUID PRIMARY KEY,
                                            content TEXT,
                                            metadata JSONB,
                                            embedding VECTOR(768)
);