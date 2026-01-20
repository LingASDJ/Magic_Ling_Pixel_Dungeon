return {
  version = "1.10",
  luaversion = "5.1",
  tiledversion = "1.11.2",
  class = "",
  orientation = "orthogonal",
  renderorder = "right-down",
  width = 7,
  height = 7,
  tilewidth = 16,
  tileheight = 16,
  nextlayerid = 2,
  nextobjectid = 1,
  properties = {},
  tilesets = {
    {
      name = "tiles_ghost",
      firstgid = 1,
      filename = "tiles_ghost.tsx"
    }
  },
  layers = {
    {
      type = "tilelayer",
      x = 0,
      y = 0,
      width = 7,
      height = 7,
      id = 1,
      name = "图块层 1",
      class = "",
      visible = true,
      opacity = 1,
      offsetx = 0,
      offsety = 0,
      parallaxx = 1,
      parallaxy = 1,
      properties = {},
      encoding = "lua",
      data = {
         5, 5, 5,5, 5,5, 5, 5, 5,
        5, 49, 49, 49, 59, 49, 49, 49, 5,
        5, 49, 73, 73, 1, 73, 73, 49, 5,
        5, 49, 73, 1, 1, 1, 73, 49, 5,
        5, 49, 67, 5, 21, 5, 67, 49, 5,
        5, 49, 67, 5, 5, 5, 67, 49, 5,
        5, 49, 73, 67, 67, 67, 73, 49, 5,
        5, 49, 49, 49, 49, 49, 49, 49, 5,
        5, 5, 5,5, 5,5, 5, 5, 5
      }
    }
  }
}
