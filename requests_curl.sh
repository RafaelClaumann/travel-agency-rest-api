# 1. Cadastrar destino — sucesso
curl -s -X POST http://localhost:8080/destination \
  -H "Content-Type: application/json" \
  -d '{"name": "Gramado", "location": "Rio Grande do Sul, Brasil", "description": "Cidade serrana com arquitetura europeia e clima frio."}'

# 2. Cadastrar destino — sem description (campo opcional, deve retornar 201)
curl -s -X POST http://localhost:8080/destination \
  -H "Content-Type: application/json" \
  -d '{"name": "Bonito", "location": "Mato Grosso do Sul, Brasil"}'

# 3. Cadastrar destino — sem name (campo obrigatório, deve retornar 422)
curl -s -X POST http://localhost:8080/destination \
  -H "Content-Type: application/json" \
  -d '{"location": "Rio de Janeiro, Brasil", "description": "Sem nome informado."}'

# 4. Cadastrar destino — sem location (campo obrigatório, deve retornar 422)
curl -s -X POST http://localhost:8080/destination \
  -H "Content-Type: application/json" \
  -d '{"name": "Arraial do Cabo", "description": "Sem localização informada."}'

# 5. Cadastrar destino — name em branco (deve retornar 422)
curl -s -X POST http://localhost:8080/destination \
  -H "Content-Type: application/json" \
  -d '{"name": "", "location": "Bahia, Brasil"}'

# 6. Listar todos os destinos
curl -s -X GET http://localhost:8080/destination

# 7. Pesquisar por nome — retorna apenas destinos com "Gramado" no nome
curl -s -X GET "http://localhost:8080/destination/search?name=Gramado"

# 8. Pesquisar por localização — retorna apenas destinos com "Sul" na localização
curl -s -X GET "http://localhost:8080/destination/search?location=Sul"

# 9. Pesquisar por nome e localização — filtra pelos dois campos simultaneamente
curl -s -X GET "http://localhost:8080/destination/search?name=Gramado&location=Rio+Grande+do+Sul"

# 10. Pesquisar sem parâmetros — deve retornar todos os destinos
curl -s -X GET "http://localhost:8080/destination/search"

# 11. Buscar destino por ID — sucesso
curl -s -X GET http://localhost:8080/destination/1

# 12. Buscar destino por ID inexistente — deve retornar 404
curl -s -X GET http://localhost:8080/destination/999

# 13. Avaliar destino — sucesso, nota válida
curl -s -X PATCH http://localhost:8080/destination/1/rating \
  -H "Content-Type: application/json" \
  -d '{"rating": 9.5}'

# 14. Avaliar destino — nota acima do máximo (deve retornar 422)
curl -s -X PATCH http://localhost:8080/destination/1/rating \
  -H "Content-Type: application/json" \
  -d '{"rating": 11}'

# 15. Avaliar destino — nota abaixo do mínimo (deve retornar 422)
curl -s -X PATCH http://localhost:8080/destination/1/rating \
  -H "Content-Type: application/json" \
  -d '{"rating": 0}'

# 16. Avaliar destino — sem rating no body (deve retornar 422)
curl -s -X PATCH http://localhost:8080/destination/1/rating \
  -H "Content-Type: application/json" \
  -d '{}'

# 17. Avaliar destino inexistente — deve retornar 404
curl -s -X PATCH http://localhost:8080/destination/999/rating \
  -H "Content-Type: application/json" \
  -d '{"rating": 8}'

# 18. Excluir destino — sucesso, deve retornar 204
curl -s -X DELETE http://localhost:8080/destination/1

# 19. Excluir destino inexistente — deve retornar 404
curl -s -X DELETE http://localhost:8080/destination/999
