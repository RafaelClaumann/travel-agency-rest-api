# 1. Cadastrar destino
curl -s -X POST http://localhost:8080/destination \
  -H "Content-Type: application/json" \
  -d '{"name": "Gramado", "location": "Rio Grande do Sul, Brasil", "description": "Cidade serrana com arquitetura europeia e clima frio."}'

# 2. Listar todos os destinos
curl -s -X GET http://localhost:8080/destination

# 3. Pesquisar por nome — retorna apenas destinos com "Gramado" no nome
curl -s -X GET "http://localhost:8080/destination/search?name=Gramado"

# 4. Pesquisar por localização — retorna apenas destinos com "Bahia" na localização
curl -s -X GET "http://localhost:8080/destination/search?location=Bahia"

# 5. Pesquisar por nome e localização — filtra pelos dois campos ao mesmo tempo
curl -s -X GET "http://localhost:8080/destination/search?name=Chapada&location=Bahia"

# 6. Buscar destino por ID — retorna 404 se não existir
curl -s -X GET http://localhost:8080/destination/1

# 7. Buscar destino por ID inexistente — deve retornar 404 Not Found
curl -s -X GET http://localhost:8080/destination/999

# 8. Avaliar destino — nota válida entre 1 e 10
curl -s -X PATCH http://localhost:8080/destination/1/rating \
  -H "Content-Type: application/json" \
  -d '{"rating": 9.5}'

# 9. Avaliar destino — nota inválida, deve retornar 400 Bad Request
curl -s -X PATCH http://localhost:8080/destination/1/rating \
  -H "Content-Type: application/json" \
  -d '{"rating": 11}'

# 10. Excluir destino — retorna 204 No Content se bem-sucedido
curl -s -X DELETE http://localhost:8080/destination/1

# 11. Excluir destino inexistente — deve retornar 404 Not Found
curl -s -X DELETE http://localhost:8080/destination/999
