# 1. Cadastrar destino — sucesso
echo "1. POST /destination — Cadastrar destino com todos os campos"
curl -s -X POST http://localhost:8080/destination \
  -H "Content-Type: application/json" \
  -d '{"name": "Gramado", "location": "Rio Grande do Sul, Brasil", "description": "Cidade serrana com arquitetura europeia e clima frio."}'
echo ""
echo "--------------------------------------------"

# 2. Cadastrar destino — sem description (campo opcional, deve retornar 201)
echo "2. POST /destination — Cadastrar destino sem description (campo opcional, esperado 201)"
curl -s -X POST http://localhost:8080/destination \
  -H "Content-Type: application/json" \
  -d '{"name": "Bonito", "location": "Mato Grosso do Sul, Brasil"}'
echo ""
echo "--------------------------------------------"

# 3. Cadastrar destino — sem name (campo obrigatório, deve retornar 422)
echo "3. POST /destination — Cadastrar destino sem name (campo obrigatório, esperado 422)"
curl -s -X POST http://localhost:8080/destination \
  -H "Content-Type: application/json" \
  -d '{"location": "Rio de Janeiro, Brasil", "description": "Sem nome informado."}'
echo ""
echo "--------------------------------------------"

# 4. Cadastrar destino — sem location (campo obrigatório, deve retornar 422)
echo "4. POST /destination — Cadastrar destino sem location (campo obrigatório, esperado 422)"
curl -s -X POST http://localhost:8080/destination \
  -H "Content-Type: application/json" \
  -d '{"name": "Arraial do Cabo", "description": "Sem localização informada."}'
echo ""
echo "--------------------------------------------"

# 5. Cadastrar destino — name em branco (deve retornar 422)
echo "5. POST /destination — Cadastrar destino com name em branco (esperado 422)"
curl -s -X POST http://localhost:8080/destination \
  -H "Content-Type: application/json" \
  -d '{"name": "", "location": "Bahia, Brasil"}'
echo ""
echo "--------------------------------------------"

echo "6. GET /destination — Listar todos os destinos"
curl -s -X GET http://localhost:8080/destination
echo ""
echo "--------------------------------------------"

echo "7. GET /destination/search?name=Gramado — Pesquisar por nome"
curl -s -X GET "http://localhost:8080/destination/search?name=Gramado"
echo ""
echo "--------------------------------------------"

echo "8. GET /destination/search?location=Sul — Pesquisar por localização"
curl -s -X GET "http://localhost:8080/destination/search?location=Sul"
echo ""
echo "--------------------------------------------"

echo "9. GET /destination/search?name=Gramado&location=Rio+Grande+do+Sul — Pesquisar por nome e localização"
curl -s -X GET "http://localhost:8080/destination/search?name=Gramado&location=Rio+Grande+do+Sul"
echo ""
echo "--------------------------------------------"

echo "10. GET /destination/search — Pesquisar sem parâmetros (esperado retornar todos)"
curl -s -X GET "http://localhost:8080/destination/search"
echo ""
echo "--------------------------------------------"

echo "11. GET /destination/1 — Buscar destino por ID existente (esperado 200)"
curl -s -X GET http://localhost:8080/destination/1
echo ""
echo "--------------------------------------------"

echo "12. GET /destination/999 — Buscar destino por ID inexistente (esperado 404)"
curl -s -X GET http://localhost:8080/destination/999
echo ""
echo "--------------------------------------------"

echo "13. PATCH /destination/1/rating — Avaliar destino com nota válida (esperado 200)"
curl -s -X PATCH http://localhost:8080/destination/1/rating \
  -H "Content-Type: application/json" \
  -d '{"rating": 9.5}'
echo ""
echo "--------------------------------------------"

echo "14. PATCH /destination/1/rating — Nota acima do máximo (esperado 422)"
curl -s -X PATCH http://localhost:8080/destination/1/rating \
  -H "Content-Type: application/json" \
  -d '{"rating": 11}'
echo ""
echo "--------------------------------------------"

echo "15. PATCH /destination/1/rating — Nota abaixo do mínimo (esperado 422)"
curl -s -X PATCH http://localhost:8080/destination/1/rating \
  -H "Content-Type: application/json" \
  -d '{"rating": 0}'
echo ""
echo "--------------------------------------------"

echo "16. PATCH /destination/1/rating — Sem rating no body (esperado 422)"
curl -s -X PATCH http://localhost:8080/destination/1/rating \
  -H "Content-Type: application/json" \
  -d '{}'
echo ""
echo "--------------------------------------------"

echo "17. PATCH /destination/999/rating — Avaliar destino inexistente (esperado 404)"
curl -s -X PATCH http://localhost:8080/destination/999/rating \
  -H "Content-Type: application/json" \
  -d '{"rating": 8}'
echo ""
echo "--------------------------------------------"

echo "18. DELETE /destination/1 — Excluir destino existente (esperado 204)"
curl -s -X DELETE http://localhost:8080/destination/1
echo ""
echo "--------------------------------------------"

echo "19. DELETE /destination/999 — Excluir destino inexistente (esperado 404)"
curl -s -X DELETE http://localhost:8080/destination/999
echo ""
echo "--------------------------------------------"
