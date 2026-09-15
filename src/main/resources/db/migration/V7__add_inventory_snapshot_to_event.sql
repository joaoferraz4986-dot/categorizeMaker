ALTER TABLE evento ADD COLUMN total_quantidade INT NULL;
ALTER TABLE evento ADD COLUMN quantidade_livre INT NULL;
ALTER TABLE evento ADD COLUMN quantidade_usado INT NULL;
ALTER TABLE evento ADD COLUMN quantidade_quebrado INT NULL;

UPDATE evento e
JOIN (
    SELECT
        COALESCE(SUM(quantidade), 0) AS total_quantidade,
        COALESCE(SUM(CASE WHEN estado = 'LIVRE' THEN quantidade ELSE 0 END), 0) AS quantidade_livre,
        COALESCE(SUM(CASE WHEN estado = 'USADO' THEN quantidade ELSE 0 END), 0) AS quantidade_usado,
        COALESCE(SUM(CASE WHEN estado = 'QUEBRADO' THEN quantidade ELSE 0 END), 0) AS quantidade_quebrado
    FROM item
) s ON 1 = 1
SET e.total_quantidade = s.total_quantidade,
    e.quantidade_livre = s.quantidade_livre,
    e.quantidade_usado = s.quantidade_usado,
    e.quantidade_quebrado = s.quantidade_quebrado
WHERE e.entidade = 'ITEM';
