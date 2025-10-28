return {
  version = "1.10",
  luaversion = "5.1",
  tiledversion = "1.11.2",
  class = "",
  orientation = "orthogonal",
  renderorder = "right-down",
  width = 11,
  height = 11,
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
      width = 11,
      height = 11,
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
        70, 70, 1, 70, 70, 1, 1, 1, 70, 1, 1,
        70, 49, 49, 57, 49, 49, 49, 57, 49, 49, 1,
        1, 49, 74, 5, 5, 49, 5, 5, 74, 49, 1,
        70, 5, 5, 5, 49, 49, 49, 5, 5, 5, 70,
        1, 1, 1, 67, 67, 1, 1, 1, 1, 70, 70,
        1, 1, 49, 67, 1, 5, 1, 1, 49, 1, 1,
        1, 5, 74, 5, 1, 70, 70, 5, 74, 5, 1,
        70, 5, 5, 5, 49, 49, 49, 5, 5, 5, 1,
        70, 49, 5, 5, 1, 49, 67, 5, 5, 49, 1,
        1, 49, 49, 57, 81, 49, 81, 57, 49, 49, 70,
        1, 70, 70, 1, 70, 1, 1, 1, 70, 70, 1
      }
    }
  }
}
